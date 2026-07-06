package com.erasm.service;
import com.erasm.config.JwtConfig; import com.erasm.dto.request.*; import com.erasm.dto.response.AuthResponse; import com.erasm.entity.*; import com.erasm.exception.*; import com.erasm.repository.*; import com.erasm.security.*; import org.slf4j.*; import org.springframework.beans.factory.annotation.Value; import org.springframework.security.authentication.BadCredentialsException; import org.springframework.security.core.userdetails.UserDetails; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.time.LocalDateTime;

@Service public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepo; private final RoleRepository roleRepo; private final RefreshTokenRepository refreshRepo; private final BlacklistedTokenRepository blacklistRepo;
    private final PasswordEncoder encoder; private final JwtTokenProvider jwt; private final CustomUserDetailsService uds; private final AuditService audit; private final JwtConfig jwtConfig;
    @Value("${security.account.max-failed-attempts:5}") private int maxFailed;


    public AuthService(UserRepository userRepo, RoleRepository roleRepo, RefreshTokenRepository refreshRepo, BlacklistedTokenRepository blacklistRepo, PasswordEncoder encoder, JwtTokenProvider jwt, CustomUserDetailsService uds, AuditService audit, JwtConfig jwtConfig) {
        this.userRepo=userRepo; this.roleRepo=roleRepo; this.refreshRepo=refreshRepo; this.blacklistRepo=blacklistRepo; this.encoder=encoder; this.jwt=jwt; this.uds=uds; this.audit=audit; this.jwtConfig=jwtConfig;
    }

    @Transactional public AuthResponse register(RegisterRequest req) {
        String email = req.getEmail().toLowerCase().trim();
        if (userRepo.existsByEmailIgnoreCase(email)) throw new DuplicateResourceException("Email already registered: " + email);
        Role role = roleRepo.findByName(req.getRole().toUpperCase()).orElseThrow(() -> new IllegalArgumentException("Invalid role: " + req.getRole()));
        User user = userRepo.save(User.builder().email(email).password(encoder.encode(req.getPassword())).role(role).isActive(true).failedLoginAttempts(0).accountLocked(false).build());
        UserDetails ud = uds.loadUserByUsername(email); String at = jwt.generateAccessToken(ud); String rt = jwt.generateRefreshToken(email);
        refreshRepo.save(RefreshToken.builder().user(user).token(rt).expiryDate(LocalDateTime.now().plusDays(7)).createdAt(LocalDateTime.now()).build());
        audit.log("USER", user.getId(), "CREATE", email, "User registered with role: " + role.getName());
        return AuthResponse.builder().accessToken(at).refreshToken(rt).tokenType("Bearer").expiresIn(jwtConfig.getAccessTokenExpiry()).email(email).role(role.getName()).build();
    }

    @Transactional public AuthResponse login(LoginRequest req) {
        String email = req.getEmail().toLowerCase().trim();
        User user = userRepo.findByEmailIgnoreCase(email).orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        if (user.getAccountLocked()) { throw new AccountLockedException("Account locked. Please contact an administrator to unlock."); }
        if (!user.getIsActive()) throw new UnauthorizedAccessException("Account deactivated.");
        if (!encoder.matches(req.getPassword(), user.getPassword())) { user.setFailedLoginAttempts(user.getFailedLoginAttempts()+1); if (user.getFailedLoginAttempts()>=maxFailed) { user.setAccountLocked(true); user.setLockedAt(LocalDateTime.now()); userRepo.save(user); throw new AccountLockedException("Account locked after " + maxFailed + " failed attempts."); } userRepo.save(user); throw new BadCredentialsException("Invalid password"); }
        user.setFailedLoginAttempts(0); userRepo.save(user);
        UserDetails ud = uds.loadUserByUsername(email); String at = jwt.generateAccessToken(ud); String rt = jwt.generateRefreshToken(email);
        refreshRepo.save(RefreshToken.builder().user(user).token(rt).expiryDate(LocalDateTime.now().plusDays(7)).createdAt(LocalDateTime.now()).build());
        audit.log("USER", user.getId(), "LOGIN", email, "Login successful");
        return AuthResponse.builder().accessToken(at).refreshToken(rt).tokenType("Bearer").expiresIn(jwtConfig.getAccessTokenExpiry()).email(email).role(user.getRole().getName()).build();
    }

    @Transactional public void logout(String accessToken, String email) { blacklistRepo.save(BlacklistedToken.builder().token(accessToken).tokenType("ACCESS").blacklistedAt(LocalDateTime.now()).expiresAt(LocalDateTime.now().plusMinutes(jwtConfig.getAccessTokenExpiry()/60000)).build()); audit.log("USER",null,"LOGOUT",email,"Logged out"); }

    @Transactional public void changePassword(String email, ChangePasswordRequest req) {
        User user = userRepo.findByEmailIgnoreCase(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!encoder.matches(req.getOldPassword(), user.getPassword())) throw new BadCredentialsException("Old password incorrect");
        if (encoder.matches(req.getNewPassword(), user.getPassword())) throw new IllegalArgumentException("New password same as old");
        user.setPassword(encoder.encode(req.getNewPassword())); userRepo.save(user); audit.log("USER",user.getId(),"UPDATE",email,"Password changed");
    }

    @Transactional public AuthResponse refreshToken(String rtStr) {
        if (!jwt.validateToken(rtStr)) throw new BadCredentialsException("Invalid refresh token");
        if (blacklistRepo.existsByToken(rtStr)) throw new BadCredentialsException("Refresh token invalidated");
        RefreshToken stored = refreshRepo.findByToken(rtStr).orElseThrow(() -> new BadCredentialsException("Refresh token not found"));
        if (stored.getExpiryDate().isBefore(LocalDateTime.now())) { refreshRepo.delete(stored); throw new BadCredentialsException("Refresh token expired"); }
        String email = jwt.getEmailFromToken(rtStr); UserDetails ud = uds.loadUserByUsername(email);
        User user = userRepo.findByEmailIgnoreCase(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        return AuthResponse.builder().accessToken(jwt.generateAccessToken(ud)).refreshToken(rtStr).tokenType("Bearer").expiresIn(jwtConfig.getAccessTokenExpiry()).email(email).role(user.getRole().getName()).build();
    }
}

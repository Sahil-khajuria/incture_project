package com.erasm.service;

import com.erasm.config.JwtConfig;
import com.erasm.dto.request.LoginRequest;
import com.erasm.dto.request.RegisterRequest;
import com.erasm.dto.response.AuthResponse;
import com.erasm.entity.Role;
import com.erasm.entity.User;
import com.erasm.exception.AccountLockedException;
import com.erasm.exception.DuplicateResourceException;
import com.erasm.exception.UserNotFoundException;
import com.erasm.repository.*;
import com.erasm.security.CustomUserDetailsService;
import com.erasm.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private BlacklistedTokenRepository blacklistedTokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private CustomUserDetailsService userDetailsService;
    @Mock private AuditService auditService;
    @Mock private JwtConfig jwtConfig;
    @InjectMocks private AuthService authService;

    private Role adminRole;
    private User testUser;
    private UserDetails testUserDetails;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "maxFailed", 5);


        adminRole = Role.builder().id(1L).name("ADMIN").build();
        testUser = User.builder().id(1L).email("test@erasm.com").password("encoded").role(adminRole).isActive(true).failedLoginAttempts(0).accountLocked(false).build();
        testUserDetails = new org.springframework.security.core.userdetails.User("test@erasm.com", "encoded", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test @DisplayName("Register - success")
    void register_Success() {
        RegisterRequest req = RegisterRequest.builder().email("new@erasm.com").password("Pass@123").role("ADMIN").build();
        when(userRepository.existsByEmailIgnoreCase("new@erasm.com")).thenReturn(false);
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> { User u = i.getArgument(0); u.setId(1L); return u; });
        when(userDetailsService.loadUserByUsername("new@erasm.com")).thenReturn(testUserDetails);
        when(jwtTokenProvider.generateAccessToken(any())).thenReturn("access-token");
        when(jwtTokenProvider.generateRefreshToken(any())).thenReturn("refresh-token");
        when(jwtConfig.getAccessTokenExpiry()).thenReturn(900000L);

        AuthResponse response = authService.register(req);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRole()).isEqualTo("ADMIN");
        verify(userRepository).save(any(User.class));
        verify(auditService).log(eq("USER"), anyLong(), eq("CREATE"), eq("new@erasm.com"), anyString());
    }

    @Test @DisplayName("Register - duplicate email throws")
    void register_DuplicateEmail() {
        RegisterRequest req = RegisterRequest.builder().email("exists@erasm.com").password("Pass@123").role("ADMIN").build();
        when(userRepository.existsByEmailIgnoreCase("exists@erasm.com")).thenReturn(true);
        assertThatThrownBy(() -> authService.register(req)).isInstanceOf(DuplicateResourceException.class);
    }

    @Test @DisplayName("Login - success")
    void login_Success() {
        LoginRequest req = LoginRequest.builder().email("test@erasm.com").password("Pass@123").build();
        when(userRepository.findByEmailIgnoreCase("test@erasm.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("Pass@123", "encoded")).thenReturn(true);
        when(userDetailsService.loadUserByUsername("test@erasm.com")).thenReturn(testUserDetails);
        when(jwtTokenProvider.generateAccessToken(any())).thenReturn("access-token");
        when(jwtTokenProvider.generateRefreshToken(any())).thenReturn("refresh-token");
        when(jwtConfig.getAccessTokenExpiry()).thenReturn(900000L);
        when(userRepository.save(any())).thenReturn(testUser);

        AuthResponse response = authService.login(req);

        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getEmail()).isEqualTo("test@erasm.com");
        verify(auditService).log(eq("USER"), eq(1L), eq("LOGIN"), eq("test@erasm.com"), anyString());
    }

    @Test @DisplayName("Login - user not found")
    void login_UserNotFound() {
        when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authService.login(LoginRequest.builder().email("no@erasm.com").password("x").build()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test @DisplayName("Login - wrong password increments counter")
    void login_WrongPassword() {
        when(userRepository.findByEmailIgnoreCase("test@erasm.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(testUser);

        assertThatThrownBy(() -> authService.login(LoginRequest.builder().email("test@erasm.com").password("wrong").build()))
                .isInstanceOf(BadCredentialsException.class);
        assertThat(testUser.getFailedLoginAttempts()).isEqualTo(1);
    }

    @Test @DisplayName("Login - account locked after max attempts")
    void login_AccountLocksAfterMaxAttempts() {
        testUser.setFailedLoginAttempts(4);
        when(userRepository.findByEmailIgnoreCase("test@erasm.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(testUser);

        assertThatThrownBy(() -> authService.login(LoginRequest.builder().email("test@erasm.com").password("wrong").build()))
                .isInstanceOf(AccountLockedException.class);
        assertThat(testUser.getAccountLocked()).isTrue();
    }

    @Test @DisplayName("Login - locked account throws")
    void login_LockedAccount() {
        testUser.setAccountLocked(true);
        testUser.setLockedAt(java.time.LocalDateTime.now());
        when(userRepository.findByEmailIgnoreCase("test@erasm.com")).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> authService.login(LoginRequest.builder().email("test@erasm.com").password("x").build()))
                .isInstanceOf(AccountLockedException.class);
    }
}

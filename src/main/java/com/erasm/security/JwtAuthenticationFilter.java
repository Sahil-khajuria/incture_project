package com.erasm.security;
import com.erasm.repository.BlacklistedTokenRepository; import jakarta.servlet.*; import jakarta.servlet.http.*; import org.slf4j.*; import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.security.core.userdetails.UserDetails; import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; import org.springframework.stereotype.Component; import org.springframework.util.StringUtils; import org.springframework.web.filter.OncePerRequestFilter; import java.io.IOException;

@Component public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider; private final CustomUserDetailsService userDetailsService; private final BlacklistedTokenRepository blacklistedTokenRepository;
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService, BlacklistedTokenRepository blacklistedTokenRepository) { this.jwtTokenProvider = jwtTokenProvider; this.userDetailsService = userDetailsService; this.blacklistedTokenRepository = blacklistedTokenRepository; }

    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try { String token = extractToken(request); if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) { if (blacklistedTokenRepository.existsByToken(token)) { filterChain.doFilter(request, response); return; } String email = jwtTokenProvider.getEmailFromToken(token); UserDetails ud = userDetailsService.loadUserByUsername(email); UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities()); auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); SecurityContextHolder.getContext().setAuthentication(auth); }
        } catch (Exception ex) { logger.warn("Auth error: {}", ex.getMessage()); }
        filterChain.doFilter(request, response);
    }
    private String extractToken(HttpServletRequest r) { String h = r.getHeader("Authorization"); return StringUtils.hasText(h) && h.startsWith("Bearer ") ? h.substring(7) : null; }
}

package com.erasm.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "blacklisted_tokens") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BlacklistedToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 500) private String token;
    @Column(name = "token_type", nullable = false, length = 20) @Builder.Default private String tokenType = "ACCESS";
    @Column(name = "blacklisted_at") @Builder.Default private LocalDateTime blacklistedAt = LocalDateTime.now();
    @Column(name = "expires_at", nullable = false) private LocalDateTime expiresAt;
}

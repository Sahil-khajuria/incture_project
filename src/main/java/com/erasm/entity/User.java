package com.erasm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity @Table(name = "users") @EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 150) private String email;
    @Column(nullable = false, length = 255) private String password;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "role_id", nullable = false) private Role role;
    @Column(name = "is_active") @Builder.Default private Boolean isActive = true;
    @Column(name = "failed_login_attempts") @Builder.Default private Integer failedLoginAttempts = 0;
    @Column(name = "account_locked") @Builder.Default private Boolean accountLocked = false;
    @Column(name = "locked_at") private LocalDateTime lockedAt;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private Employee employee;
    @CreatedDate @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @LastModifiedDate @Column(name = "updated_at") private LocalDateTime updatedAt;
    @CreatedBy @Column(name = "created_by", length = 150) private String createdBy;
    @LastModifiedBy @Column(name = "modified_by", length = 150) private String modifiedBy;
}

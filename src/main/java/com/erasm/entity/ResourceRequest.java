package com.erasm.entity;
import com.erasm.enums.RequestStatus;
import com.erasm.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name = "resource_requests") @EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResourceRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "project_id", nullable = false) private Project project;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "requested_by", nullable = false) private User requestedBy;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "skill_id", nullable = false) private Skill skill;
    @Enumerated(EnumType.STRING) @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR) @Column(name = "skill_level", length = 20) private SkillLevel skillLevel;
    @Column(name = "required_count", nullable = false) @Builder.Default private Integer requiredCount = 1;
    @Enumerated(EnumType.STRING) @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR) @Column(length = 50) @Builder.Default private RequestStatus status = RequestStatus.DRAFT;
    @Column(columnDefinition = "TEXT") private String remarks;
    @Column(name = "requested_start_date") private LocalDate requestedStartDate;
    @Column(name = "requested_end_date") private LocalDate requestedEndDate;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "reviewed_by") private User reviewedBy;
    @Column(name = "reviewed_at") private LocalDateTime reviewedAt;
    @CreatedDate @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @LastModifiedDate @Column(name = "updated_at") private LocalDateTime updatedAt;
}

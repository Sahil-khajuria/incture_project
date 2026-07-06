package com.erasm.entity;
import com.erasm.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "projects") @EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 200) private String name;
    @Column(name = "client_name", length = 200) private String clientName;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(name = "start_date", nullable = false) private LocalDate startDate;
    @Column(name = "end_date") private LocalDate endDate;
    @Enumerated(EnumType.STRING) @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR) @Column(length = 50) @Builder.Default private ProjectStatus status = ProjectStatus.ACTIVE;
    @Column(name = "technology_stack", columnDefinition = "TEXT") private String technologyStack;
    @Column(precision = 15, scale = 2) private BigDecimal budget;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "created_by") private User createdByUser;
    @OneToMany(mappedBy = "project") @Builder.Default private List<ResourceRequest> resourceRequests = new ArrayList<>();
    @OneToMany(mappedBy = "project") @Builder.Default private List<Allocation> allocations = new ArrayList<>();
    @CreatedDate @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @LastModifiedDate @Column(name = "updated_at") private LocalDateTime updatedAt;
    @LastModifiedBy @Column(name = "modified_by", length = 150) private String modifiedBy;
}

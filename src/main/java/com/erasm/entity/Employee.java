package com.erasm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "employees") @EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false, unique = true) private User user;
    @Column(name = "first_name", nullable = false, length = 100) private String firstName;
    @Column(name = "last_name", nullable = false, length = 100) private String lastName;
    @Column(length = 20) private String phone;
    @Column(length = 100) private String department;
    @Column(length = 100) private String designation;
    @Column(name = "date_of_joining") private LocalDate dateOfJoining;
    @Column(name = "employment_type", length = 50) private String employmentType;
    @Column(name = "bench_status") @Builder.Default private Boolean benchStatus = true;
    @Column(name = "total_experience_years") @Builder.Default private Double totalExperienceYears = 0.0;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true) @Builder.Default private List<EmployeeSkill> skills = new ArrayList<>();
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true) @Builder.Default private List<Certification> certifications = new ArrayList<>();
    @OneToMany(mappedBy = "employee") @Builder.Default private List<Allocation> allocations = new ArrayList<>();
    @CreatedDate @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @LastModifiedDate @Column(name = "updated_at") private LocalDateTime updatedAt;
    @CreatedBy @Column(name = "created_by", length = 150) private String createdBy;
    @LastModifiedBy @Column(name = "modified_by", length = 150) private String modifiedBy;
}

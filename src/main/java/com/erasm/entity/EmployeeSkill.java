package com.erasm.entity;

import com.erasm.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "employee_skills", uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "skill_id"})})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeSkill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "employee_id", nullable = false) private Employee employee;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "skill_id", nullable = false) private Skill skill;
    @Enumerated(EnumType.STRING) @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR) @Column(nullable = false, length = 20) private SkillLevel level;
    @Column(name = "years_of_experience") @Builder.Default private Double yearsOfExperience = 0.0;
    @Column(name = "last_used_date") private LocalDate lastUsedDate;
}

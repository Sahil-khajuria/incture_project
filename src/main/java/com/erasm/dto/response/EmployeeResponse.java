package com.erasm.dto.response;
import lombok.*; import java.time.LocalDate; import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeResponse {
    private Long id; private Long userId; private String email; private String firstName; private String lastName; private String phone; private String department; private String designation; private LocalDate dateOfJoining; private String employmentType; private Boolean benchStatus; private Double totalExperienceYears; private List<EmployeeSkillResponse> skills; private List<CertificationResponse> certifications;
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class EmployeeSkillResponse { private Long id; private Long skillId; private String skillName; private String category; private String level; private Double yearsOfExperience; private LocalDate lastUsedDate; }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CertificationResponse { private Long id; private String name; private String issuingOrganization; private LocalDate issueDate; private LocalDate expiryDate; private String credentialId; private String credentialUrl; private boolean isExpired; }
}

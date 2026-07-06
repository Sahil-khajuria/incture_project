package com.erasm.dto.request;
import jakarta.validation.constraints.*; import lombok.*; import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeRequest { @NotBlank private String firstName; @NotBlank private String lastName; private String phone; private String department; private String designation; private LocalDate dateOfJoining; private String employmentType; private Double totalExperienceYears; private Long userId; }

package com.erasm.dto.request;
import com.erasm.enums.SkillLevel; import jakarta.validation.constraints.*; import lombok.*; import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeSkillRequest { @NotNull private Long skillId; @NotNull private SkillLevel level; private Double yearsOfExperience; private LocalDate lastUsedDate; }

package com.erasm.dto.request;
import com.erasm.enums.SkillLevel; import jakarta.validation.constraints.*; import lombok.*; import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ResourceRequestDto { @NotNull private Long projectId; @NotNull private Long skillId; private SkillLevel skillLevel; @Min(1) private Integer requiredCount; private String remarks; private LocalDate requestedStartDate; private LocalDate requestedEndDate; }

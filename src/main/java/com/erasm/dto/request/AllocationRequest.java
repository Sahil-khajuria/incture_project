package com.erasm.dto.request;
import jakarta.validation.constraints.*; import lombok.*; import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AllocationRequest { @NotNull private Long employeeId; @NotNull private Long projectId; private Long resourceRequestId; @NotNull @Min(1) @Max(100) private Integer allocationPercentage; @NotNull @FutureOrPresent(message = "Start date must be today or in the future") private LocalDate startDate; private LocalDate endDate; }

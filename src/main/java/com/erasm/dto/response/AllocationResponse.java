package com.erasm.dto.response;
import lombok.*; import java.time.LocalDate; @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AllocationResponse { private Long id; private Long employeeId; private String employeeName; private Long projectId; private String projectName; private Long resourceRequestId; private Integer allocationPercentage; private LocalDate startDate; private LocalDate endDate; private String status; private String allocatedBy; }

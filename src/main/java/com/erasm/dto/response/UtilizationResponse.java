package com.erasm.dto.response;
import lombok.*; import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UtilizationResponse {
    private Long employeeId; private String employeeName; private int totalAllocationPercentage; private int billablePercentage; private int benchPercentage; private boolean onBench; private List<ProjectAllocationSummary> projects;
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ProjectAllocationSummary { private Long projectId; private String projectName; private int allocationPercentage; }
}

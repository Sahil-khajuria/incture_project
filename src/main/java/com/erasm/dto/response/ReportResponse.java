package com.erasm.dto.response;
import lombok.*; import java.util.List; import java.util.Map;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReportResponse {
    private String skill; private Integer totalEmployees; private Map<String, Integer> byLevel; private List<EmployeeSummary> employees;
    private Integer benchCount; private Integer fullyAllocatedCount; private Integer partiallyAllocatedCount;
    private String project; private String status; private Integer allocatedEmployees;
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class EmployeeSummary { private Long id; private String name; private String level; private Integer billable; private Integer bench; private Integer allocationPercentage; }
}

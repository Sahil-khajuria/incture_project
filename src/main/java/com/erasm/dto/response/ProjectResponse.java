package com.erasm.dto.response;
import lombok.*; import java.math.BigDecimal; import java.time.LocalDate; @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectResponse { private Long id; private String name; private String clientName; private String description; private LocalDate startDate; private LocalDate endDate; private String status; private String technologyStack; private BigDecimal budget; private String createdBy; private int allocatedEmployeeCount; }

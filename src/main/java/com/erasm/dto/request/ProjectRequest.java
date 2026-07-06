package com.erasm.dto.request;
import jakarta.validation.constraints.*; import lombok.*; import java.math.BigDecimal; import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectRequest { @NotBlank private String name; private String clientName; private String description; @NotNull @FutureOrPresent(message = "Start date must be today or in the future") private LocalDate startDate; private LocalDate endDate; private String technologyStack; private BigDecimal budget; }

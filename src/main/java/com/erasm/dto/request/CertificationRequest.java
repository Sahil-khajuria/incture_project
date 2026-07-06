package com.erasm.dto.request;
import jakarta.validation.constraints.*; import lombok.*; import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CertificationRequest { @NotBlank private String name; private String issuingOrganization; @PastOrPresent private LocalDate issueDate; private LocalDate expiryDate; private String credentialId; private String credentialUrl; }

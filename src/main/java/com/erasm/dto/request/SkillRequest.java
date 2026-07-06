package com.erasm.dto.request;
import jakarta.validation.constraints.*; import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SkillRequest { @NotBlank private String name; private String category; private String description; }

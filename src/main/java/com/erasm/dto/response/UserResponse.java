package com.erasm.dto.response;
import lombok.*; import java.time.LocalDateTime; @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponse { private Long id; private String email; private String role; private Boolean isActive; private LocalDateTime createdAt; private LocalDateTime updatedAt; }

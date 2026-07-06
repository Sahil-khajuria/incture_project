package com.erasm.mapper;
import com.erasm.dto.response.UserResponse; import com.erasm.entity.User; import org.springframework.stereotype.Component;
@Component public class UserMapper {
    public UserResponse toResponse(User u) { return UserResponse.builder().id(u.getId()).email(u.getEmail()).role(u.getRole().getName()).isActive(u.getIsActive()).createdAt(u.getCreatedAt()).updatedAt(u.getUpdatedAt()).build(); }
}

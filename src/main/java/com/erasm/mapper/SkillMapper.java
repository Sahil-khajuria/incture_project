package com.erasm.mapper;
import com.erasm.dto.response.SkillResponse; import com.erasm.entity.Skill; import org.springframework.stereotype.Component;
@Component public class SkillMapper { public SkillResponse toResponse(Skill s) { return SkillResponse.builder().id(s.getId()).name(s.getName()).category(s.getCategory()).description(s.getDescription()).isActive(s.getIsActive()).build(); } }

package com.erasm.service;

import com.erasm.dto.request.SkillRequest;
import com.erasm.dto.response.SkillResponse;
import com.erasm.entity.Skill;
import com.erasm.exception.DuplicateResourceException;
import com.erasm.exception.SkillNotFoundException;
import com.erasm.mapper.SkillMapper;
import com.erasm.repository.SkillRepository;
import com.erasm.util.AuditUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {
    @Mock private SkillRepository repo;
    @Mock private SkillMapper mapper;
    @Mock private AuditService auditService;
    @Mock private AuditUtil auditUtil;
    @InjectMocks private SkillService service;

    private Skill java;

    @BeforeEach void setUp() { java = Skill.builder().id(1L).name("Java").category("Backend").isActive(true).build(); }

    @Test @DisplayName("Get all skills")
    void getAllSkills() {
        when(repo.findByIsActiveTrue()).thenReturn(List.of(java));
        when(mapper.toResponse(java)).thenReturn(SkillResponse.builder().id(1L).name("Java").build());
        List<SkillResponse> result = service.getAllSkills();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Java");
    }

    @Test @DisplayName("Create skill - success")
    void createSkill_Success() {
        SkillRequest req = SkillRequest.builder().name("Python").category("Backend").build();
        when(repo.existsByNameIgnoreCase("Python")).thenReturn(false);
        Skill saved = Skill.builder().id(2L).name("Python").category("Backend").isActive(true).build();
        when(repo.save(any())).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(SkillResponse.builder().id(2L).name("Python").build());

        SkillResponse resp = service.createSkill(req);
        assertThat(resp.getName()).isEqualTo("Python");
        verify(auditService).log(eq("SKILL"), eq(2L), eq("CREATE"), any(), anyString());
    }

    @Test @DisplayName("Create skill - duplicate throws")
    void createSkill_Duplicate() {
        when(repo.existsByNameIgnoreCase("Java")).thenReturn(true);
        assertThatThrownBy(() -> service.createSkill(SkillRequest.builder().name("Java").build())).isInstanceOf(DuplicateResourceException.class);
    }

    @Test @DisplayName("Get skill - not found throws")
    void getSkillById_NotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getSkillById(99L)).isInstanceOf(SkillNotFoundException.class);
    }

    @Test @DisplayName("Soft delete skill")
    void softDeleteSkill() {
        when(repo.findById(1L)).thenReturn(Optional.of(java));
        when(repo.save(any())).thenReturn(java);
        service.softDeleteSkill(1L);
        assertThat(java.getIsActive()).isFalse();
    }
}

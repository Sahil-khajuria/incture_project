package com.erasm.service;

import com.erasm.entity.*; import com.erasm.enums.*; import com.erasm.exception.*; import com.erasm.repository.*; import com.erasm.util.AuditUtil;
import org.junit.jupiter.api.*; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.*; import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*; import static org.mockito.ArgumentMatchers.*; import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) class ResourceRequestServiceTest {
    @Mock ResourceRequestRepository repo; @Mock ProjectRepository projRepo; @Mock SkillRepository skillRepo; @Mock UserRepository userRepo; @Mock AuditService auditService; @Mock AuditUtil auditUtil;
    @InjectMocks ResourceRequestService service;
    ResourceRequest rr;

    @BeforeEach void setUp() { rr = ResourceRequest.builder().id(1L).status(RequestStatus.DRAFT).project(Project.builder().id(1L).name("P1").build()).skill(Skill.builder().id(1L).name("Java").build()).build(); }

    @Test void submit_Success() { when(repo.findById(1L)).thenReturn(Optional.of(rr)); when(repo.save(any())).thenReturn(rr); assertThat(service.submitRequest(1L).getStatus()).isEqualTo(RequestStatus.SUBMITTED); }
    @Test void submit_InvalidState() { rr.setStatus(RequestStatus.APPROVED); when(repo.findById(1L)).thenReturn(Optional.of(rr)); assertThatThrownBy(()->service.submitRequest(1L)).isInstanceOf(InvalidStatusTransitionException.class); }
    @Test void approve_Success() { rr.setStatus(RequestStatus.SUBMITTED); when(repo.findById(1L)).thenReturn(Optional.of(rr)); when(userRepo.findByEmailIgnoreCase(any())).thenReturn(Optional.of(User.builder().id(3L).build())); when(repo.save(any())).thenReturn(rr); assertThat(service.approveRequest(1L,"rm@e.com").getStatus()).isEqualTo(RequestStatus.APPROVED); }
    @Test void reject_NoRemarks() { rr.setStatus(RequestStatus.SUBMITTED); when(repo.findById(1L)).thenReturn(Optional.of(rr)); assertThatThrownBy(()->service.rejectRequest(1L,"","rm@e.com")).isInstanceOf(AllocationException.class); }
    @Test void notFound() { when(repo.findById(99L)).thenReturn(Optional.empty()); assertThatThrownBy(()->service.getRequestById(99L)).isInstanceOf(ResourceRequestNotFoundException.class); }
}

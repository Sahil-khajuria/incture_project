package com.erasm.service;

import com.erasm.dto.request.ProjectRequest;
import com.erasm.dto.response.ProjectResponse;
import com.erasm.entity.*;
import com.erasm.enums.AllocationStatus;
import com.erasm.enums.ProjectStatus;
import com.erasm.enums.RequestStatus;
import com.erasm.exception.AllocationException;
import com.erasm.exception.ProjectNotFoundException;
import com.erasm.mapper.AllocationMapper;
import com.erasm.mapper.ProjectMapper;
import com.erasm.repository.*;
import com.erasm.util.AuditUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock private ProjectRepository projRepo;
    @Mock private AllocationRepository allocRepo;
    @Mock private ResourceRequestRepository rrRepo;
    @Mock private EmployeeRepository empRepo;
    @Mock private UserRepository userRepo;
    @Mock private ProjectMapper pMapper;
    @Mock private AllocationMapper aMapper;
    @Mock private AuditService auditService;
    @Mock private AuditUtil auditUtil;
    @InjectMocks private ProjectService service;

    private Project testProject;
    private User creator;
    private ProjectResponse testResponse;

    @BeforeEach
    void setUp() {
        Role role = Role.builder().id(2L).name("DELIVERY_MANAGER").build();
        creator = User.builder().id(1L).email("dm@erasm.com").role(role).build();
        testProject = Project.builder().id(1L).name("Project Alpha").clientName("Acme")
                .startDate(LocalDate.of(2026, 1, 1)).status(ProjectStatus.ACTIVE)
                .budget(new BigDecimal("100000")).createdByUser(creator)
                .allocations(new ArrayList<>()).resourceRequests(new ArrayList<>()).build();
        testResponse = ProjectResponse.builder().id(1L).name("Project Alpha").status("ACTIVE")
                .allocatedEmployeeCount(0).build();
    }

    @Test @DisplayName("Get all projects - paginated")
    void getAllProjects() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Project> page = new PageImpl<>(List.of(testProject), pageable, 1);
        when(projRepo.findAll(pageable)).thenReturn(page);
        when(pMapper.toResponse(testProject)).thenReturn(testResponse);

        Page<ProjectResponse> result = service.getAllProjects(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Project Alpha");
    }

    @Test @DisplayName("Get project by ID - success")
    void getProjectById_Success() {
        when(projRepo.findById(1L)).thenReturn(Optional.of(testProject));
        when(pMapper.toResponse(testProject)).thenReturn(testResponse);

        ProjectResponse result = service.getProjectById(1L);

        assertThat(result.getName()).isEqualTo("Project Alpha");
    }

    @Test @DisplayName("Get project by ID - not found throws")
    void getProjectById_NotFound() {
        when(projRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getProjectById(99L))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test @DisplayName("Create project - success")
    void createProject_Success() {
        ProjectRequest req = ProjectRequest.builder().name("New Project").clientName("Client B")
                .startDate(LocalDate.of(2026, 7, 1)).budget(new BigDecimal("50000")).build();
        when(userRepo.findByEmailIgnoreCase("dm@erasm.com")).thenReturn(Optional.of(creator));
        when(projRepo.save(any())).thenAnswer(i -> { Project p = i.getArgument(0); p.setId(2L); return p; });
        when(pMapper.toResponse(any())).thenReturn(ProjectResponse.builder().id(2L).name("New Project").status("ACTIVE").build());

        ProjectResponse result = service.createProject(req, "dm@erasm.com");

        assertThat(result.getName()).isEqualTo("New Project");
        verify(auditService).log(eq("PROJECT"), eq(2L), eq("CREATE"), eq("dm@erasm.com"), anyString());
    }

    @Test @DisplayName("Update project - success")
    void updateProject_Success() {
        ProjectRequest req = ProjectRequest.builder().name("Updated Name").build();
        when(projRepo.findById(1L)).thenReturn(Optional.of(testProject));
        when(projRepo.save(any())).thenReturn(testProject);
        when(pMapper.toResponse(any())).thenReturn(ProjectResponse.builder().id(1L).name("Updated Name").build());
        when(auditUtil.getCurrentUserEmail()).thenReturn("dm@erasm.com");

        ProjectResponse result = service.updateProject(1L, req);

        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(testProject.getName()).isEqualTo("Updated Name");
    }

    @Test @DisplayName("Soft delete project - no active allocations")
    void softDeleteProject_Success() {
        when(projRepo.findById(1L)).thenReturn(Optional.of(testProject));
        when(allocRepo.findByProjectIdAndStatus(1L, AllocationStatus.ACTIVE)).thenReturn(Collections.emptyList());
        when(projRepo.save(any())).thenReturn(testProject);
        when(auditUtil.getCurrentUserEmail()).thenReturn("admin@erasm.com");

        service.softDeleteProject(1L);

        assertThat(testProject.getStatus()).isEqualTo(ProjectStatus.CANCELLED);
    }

    @Test @DisplayName("Soft delete project - active allocations throws")
    void softDeleteProject_ActiveAllocations() {
        Allocation activeAlloc = Allocation.builder().id(1L).status(AllocationStatus.ACTIVE).build();
        when(projRepo.findById(1L)).thenReturn(Optional.of(testProject));
        when(allocRepo.findByProjectIdAndStatus(1L, AllocationStatus.ACTIVE)).thenReturn(List.of(activeAlloc));

        assertThatThrownBy(() -> service.softDeleteProject(1L))
                .isInstanceOf(AllocationException.class)
                .hasMessageContaining("active allocations");
    }

    @Test @DisplayName("Close project - releases allocations and sets bench")
    void closeProject_ReleasesAllocations() {
        Role empRole = Role.builder().id(4L).name("EMPLOYEE").build();
        User empUser = User.builder().id(5L).email("emp@erasm.com").role(empRole).isActive(true).build();
        Employee emp = Employee.builder().id(1L).user(empUser).firstName("John").lastName("Doe").benchStatus(false).build();
        Allocation alloc = Allocation.builder().id(1L).employee(emp).project(testProject)
                .allocationPercentage(100).status(AllocationStatus.ACTIVE).build();

        ResourceRequest rr = ResourceRequest.builder().id(1L).project(testProject).status(RequestStatus.ALLOCATED).build();

        when(projRepo.findById(1L)).thenReturn(Optional.of(testProject));
        when(allocRepo.findByProjectIdAndStatus(1L, AllocationStatus.ACTIVE)).thenReturn(List.of(alloc));
        when(allocRepo.save(any())).thenReturn(alloc);
        when(allocRepo.sumActiveAllocationPercentage(1L)).thenReturn(0);
        when(empRepo.save(any())).thenReturn(emp);
        when(rrRepo.findByProjectIdAndStatus(1L, RequestStatus.ALLOCATED)).thenReturn(List.of(rr));
        when(rrRepo.save(any())).thenReturn(rr);
        when(projRepo.save(any())).thenReturn(testProject);
        when(pMapper.toResponse(any())).thenReturn(ProjectResponse.builder().id(1L).status("COMPLETED").build());
        when(auditUtil.getCurrentUserEmail()).thenReturn("dm@erasm.com");

        ProjectResponse result = service.closeProject(1L);

        assertThat(result.getStatus()).isEqualTo("COMPLETED");
        assertThat(alloc.getStatus()).isEqualTo(AllocationStatus.COMPLETED);
        assertThat(emp.getBenchStatus()).isTrue();
        assertThat(rr.getStatus()).isEqualTo(RequestStatus.COMPLETED);
        verify(auditService).log(eq("PROJECT"), eq(1L), eq("UPDATE"), eq("dm@erasm.com"), anyString());
    }

    @Test @DisplayName("Get project allocations - project not found throws")
    void getProjectAllocations_NotFound() {
        when(projRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getProjectAllocations(99L))
                .isInstanceOf(ProjectNotFoundException.class);
    }
}

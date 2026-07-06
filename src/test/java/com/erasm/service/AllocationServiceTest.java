package com.erasm.service;

import com.erasm.dto.request.AllocationRequest;
import com.erasm.dto.response.AllocationResponse;
import com.erasm.entity.*;
import com.erasm.enums.*;
import com.erasm.exception.AllocationException;
import com.erasm.exception.EmployeeNotFoundException;
import com.erasm.mapper.AllocationMapper;
import com.erasm.repository.*;
import com.erasm.util.AuditUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AllocationServiceTest {

    @Mock private AllocationRepository allocRepo;
    @Mock private EmployeeRepository empRepo;
    @Mock private ProjectRepository projRepo;
    @Mock private ResourceRequestRepository rrRepo;
    @Mock private UserRepository userRepo;
    @Mock private AllocationMapper mapper;
    @Mock private AuditService auditService;
    @Mock private AuditUtil auditUtil;
    @InjectMocks private AllocationService service;

    private Employee employee;
    private Project project;
    private User allocator;
    private AllocationRequest request;

    @BeforeEach
    void setUp() {
        Role role = Role.builder().id(1L).name("RESOURCE_MANAGER").build();
        User empUser = User.builder().id(2L).email("emp@erasm.com").isActive(true).role(role).build();
        employee = Employee.builder().id(1L).user(empUser).firstName("John").lastName("Doe").benchStatus(true).build();
        project = Project.builder().id(1L).name("Project X").status(ProjectStatus.ACTIVE).build();
        allocator = User.builder().id(3L).email("rm@erasm.com").role(role).build();
        request = AllocationRequest.builder().employeeId(1L).projectId(1L).allocationPercentage(50).startDate(LocalDate.now()).build();
    }

    @Test @DisplayName("Allocate - success at 50%")
    void allocate_Success() {
        when(empRepo.findById(1L)).thenReturn(Optional.of(employee));
        when(projRepo.findById(1L)).thenReturn(Optional.of(project));
        when(userRepo.findByEmailIgnoreCase("rm@erasm.com")).thenReturn(Optional.of(allocator));
        when(allocRepo.sumActiveAllocationPercentage(1L)).thenReturn(0);
        when(allocRepo.save(any())).thenAnswer(i -> { Allocation a = i.getArgument(0); a.setId(1L); return a; });
        when(empRepo.save(any())).thenReturn(employee);
        when(mapper.toResponse(any())).thenReturn(AllocationResponse.builder().id(1L).allocationPercentage(50).build());

        AllocationResponse resp = service.allocate(request, "rm@erasm.com");

        assertThat(resp.getAllocationPercentage()).isEqualTo(50);
        verify(allocRepo).save(any());
        verify(auditService).log(eq("ALLOCATION"), anyLong(), eq("ALLOCATE"), eq("rm@erasm.com"), anyString());
    }

    @Test @DisplayName("Allocate - exceeds 100% throws")
    void allocate_Exceeds100() {
        when(empRepo.findById(1L)).thenReturn(Optional.of(employee));
        when(projRepo.findById(1L)).thenReturn(Optional.of(project));
        when(userRepo.findByEmailIgnoreCase("rm@erasm.com")).thenReturn(Optional.of(allocator));
        when(allocRepo.sumActiveAllocationPercentage(1L)).thenReturn(60);
        request.setAllocationPercentage(50);

        assertThatThrownBy(() -> service.allocate(request, "rm@erasm.com"))
                .isInstanceOf(AllocationException.class).hasMessageContaining("exceed 100%");
    }

    @Test @DisplayName("Allocate - inactive project throws")
    void allocate_InactiveProject() {
        project.setStatus(ProjectStatus.COMPLETED);
        when(empRepo.findById(1L)).thenReturn(Optional.of(employee));
        when(projRepo.findById(1L)).thenReturn(Optional.of(project));

        assertThatThrownBy(() -> service.allocate(request, "rm@erasm.com"))
                .isInstanceOf(AllocationException.class).hasMessageContaining("not ACTIVE");
    }

    @Test @DisplayName("Allocate - self-allocation throws")
    void allocate_SelfAllocation() {
        allocator.setId(2L); // same as employee's user
        when(empRepo.findById(1L)).thenReturn(Optional.of(employee));
        when(projRepo.findById(1L)).thenReturn(Optional.of(project));
        when(userRepo.findByEmailIgnoreCase("rm@erasm.com")).thenReturn(Optional.of(allocator));

        assertThatThrownBy(() -> service.allocate(request, "rm@erasm.com"))
                .isInstanceOf(AllocationException.class).hasMessageContaining("Self-allocation");
    }

    @Test @DisplayName("Release - sets bench when 0% remaining")
    void release_SetsBench() {
        Allocation alloc = Allocation.builder().id(1L).employee(employee).project(project).allocationPercentage(50).status(AllocationStatus.ACTIVE).build();
        when(allocRepo.findById(1L)).thenReturn(Optional.of(alloc));
        when(allocRepo.sumActiveAllocationPercentage(1L)).thenReturn(0);
        when(mapper.toResponse(any())).thenReturn(AllocationResponse.builder().id(1L).status("RELEASED").build());

        AllocationResponse resp = service.release(1L);

        assertThat(alloc.getStatus()).isEqualTo(AllocationStatus.RELEASED);
        assertThat(employee.getBenchStatus()).isTrue();
        verify(empRepo).save(employee);
    }

    @Test @DisplayName("Get employee allocations - not found throws")
    void getEmployeeAllocations_NotFound() {
        when(empRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getEmployeeAllocations(99L)).isInstanceOf(EmployeeNotFoundException.class);
    }
}

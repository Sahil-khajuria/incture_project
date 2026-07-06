package com.erasm.service;

import com.erasm.dto.request.EmployeeRequest;
import com.erasm.dto.request.EmployeeSkillRequest;
import com.erasm.dto.response.EmployeeResponse;
import com.erasm.entity.*;
import com.erasm.enums.SkillLevel;
import com.erasm.exception.DuplicateResourceException;
import com.erasm.exception.EmployeeNotFoundException;
import com.erasm.exception.UserNotFoundException;
import com.erasm.mapper.EmployeeMapper;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private EmployeeRepository empRepo;
    @Mock private UserRepository userRepo;
    @Mock private SkillRepository skillRepo;
    @Mock private EmployeeSkillRepository esRepo;
    @Mock private CertificationRepository certRepo;
    @Mock private EmployeeMapper mapper;
    @Mock private AuditService auditService;
    @Mock private AuditUtil auditUtil;
    @InjectMocks private EmployeeService service;

    private User testUser;
    private Employee testEmployee;
    private Skill testSkill;
    private EmployeeResponse testResponse;

    @BeforeEach
    void setUp() {
        Role role = Role.builder().id(4L).name("EMPLOYEE").build();
        testUser = User.builder().id(1L).email("john@erasm.com").password("encoded").role(role).isActive(true).build();
        testEmployee = Employee.builder().id(1L).user(testUser).firstName("John").lastName("Doe")
                .department("Engineering").designation("Developer").benchStatus(true)
                .totalExperienceYears(5.0).skills(new ArrayList<>()).certifications(new ArrayList<>()).build();
        testSkill = Skill.builder().id(1L).name("Java").category("Backend").isActive(true).build();
        testResponse = EmployeeResponse.builder().id(1L).firstName("John").lastName("Doe").userId(1L)
                .email("john@erasm.com").department("Engineering").benchStatus(true).build();
    }

    @Test @DisplayName("Get all employees - paginated")
    void getAllEmployees() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Employee> page = new PageImpl<>(List.of(testEmployee), pageable, 1);
        when(empRepo.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(testEmployee)).thenReturn(testResponse);

        Page<EmployeeResponse> result = service.getAllEmployees(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("John");
    }

    @Test @DisplayName("Get employee by ID - success")
    void getEmployeeById_Success() {
        when(empRepo.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(mapper.toResponse(testEmployee)).thenReturn(testResponse);

        EmployeeResponse result = service.getEmployeeById(1L);

        assertThat(result.getFirstName()).isEqualTo("John");
    }

    @Test @DisplayName("Get employee by ID - not found throws")
    void getEmployeeById_NotFound() {
        when(empRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getEmployeeById(99L))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test @DisplayName("Create employee - success")
    void createEmployee_Success() {
        EmployeeRequest req = EmployeeRequest.builder().userId(1L).firstName("Jane").lastName("Smith")
                .department("QA").designation("Tester").build();
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(empRepo.existsByUserId(1L)).thenReturn(false);
        when(empRepo.save(any())).thenAnswer(i -> { Employee e = i.getArgument(0); e.setId(2L); return e; });
        when(mapper.toResponse(any())).thenReturn(EmployeeResponse.builder().id(2L).firstName("Jane").build());
        when(auditUtil.getCurrentUserEmail()).thenReturn("admin@erasm.com");

        EmployeeResponse result = service.createEmployee(req);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        verify(auditService).log(eq("EMPLOYEE"), eq(2L), eq("CREATE"), anyString(), anyString());
    }

    @Test @DisplayName("Create employee - user not found throws")
    void createEmployee_UserNotFound() {
        EmployeeRequest req = EmployeeRequest.builder().userId(99L).firstName("X").lastName("Y").build();
        when(userRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.createEmployee(req))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test @DisplayName("Create employee - duplicate profile throws")
    void createEmployee_Duplicate() {
        EmployeeRequest req = EmployeeRequest.builder().userId(1L).firstName("X").lastName("Y").build();
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(empRepo.existsByUserId(1L)).thenReturn(true);
        assertThatThrownBy(() -> service.createEmployee(req))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test @DisplayName("Update employee - success")
    void updateEmployee_Success() {
        EmployeeRequest req = EmployeeRequest.builder().firstName("Updated").lastName("Doe").build();
        when(empRepo.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(empRepo.save(any())).thenReturn(testEmployee);
        when(mapper.toResponse(any())).thenReturn(EmployeeResponse.builder().id(1L).firstName("Updated").build());
        when(auditUtil.getCurrentUserEmail()).thenReturn("admin@erasm.com");

        EmployeeResponse result = service.updateEmployee(1L, req);

        assertThat(result.getFirstName()).isEqualTo("Updated");
        verify(mapper).updateEntity(testEmployee, req);
    }

    @Test @DisplayName("Soft delete employee - deactivates user")
    void softDeleteEmployee() {
        when(empRepo.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(userRepo.save(any())).thenReturn(testUser);
        when(auditUtil.getCurrentUserEmail()).thenReturn("admin@erasm.com");

        service.softDeleteEmployee(1L);

        assertThat(testUser.getIsActive()).isFalse();
        verify(userRepo).save(testUser);
    }

    @Test @DisplayName("Add skill to employee - success")
    void addSkill_Success() {
        EmployeeSkillRequest req = EmployeeSkillRequest.builder().skillId(1L).level(SkillLevel.INTERMEDIATE)
                .yearsOfExperience(3.0).build();
        when(empRepo.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(skillRepo.findById(1L)).thenReturn(Optional.of(testSkill));
        when(esRepo.existsByEmployeeIdAndSkillId(1L, 1L)).thenReturn(false);
        EmployeeSkill saved = EmployeeSkill.builder().id(1L).employee(testEmployee).skill(testSkill)
                .level(SkillLevel.INTERMEDIATE).yearsOfExperience(3.0).build();
        when(esRepo.save(any())).thenReturn(saved);
        when(auditUtil.getCurrentUserEmail()).thenReturn("admin@erasm.com");

        EmployeeResponse.EmployeeSkillResponse result = service.addSkillToEmployee(1L, req);

        assertThat(result.getSkillName()).isEqualTo("Java");
        assertThat(result.getLevel()).isEqualTo("INTERMEDIATE");
    }

    @Test @DisplayName("Add duplicate skill throws")
    void addSkill_Duplicate() {
        EmployeeSkillRequest req = EmployeeSkillRequest.builder().skillId(1L).level(SkillLevel.BEGINNER).build();
        when(empRepo.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(skillRepo.findById(1L)).thenReturn(Optional.of(testSkill));
        when(esRepo.existsByEmployeeIdAndSkillId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> service.addSkillToEmployee(1L, req))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test @DisplayName("Get bench employees")
    void getBenchEmployees() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Employee> page = new PageImpl<>(List.of(testEmployee), pageable, 1);
        when(empRepo.findByBenchStatusTrue(pageable)).thenReturn(page);
        when(mapper.toResponse(testEmployee)).thenReturn(testResponse);

        Page<EmployeeResponse> result = service.getBenchEmployees(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getBenchStatus()).isTrue();
    }

    @Test @DisplayName("Remove skill from employee")
    void removeSkill() {
        EmployeeSkill es = EmployeeSkill.builder().id(1L).employee(testEmployee).skill(testSkill)
                .level(SkillLevel.ADVANCED).build();
        when(esRepo.findByEmployeeIdAndSkillId(1L, 1L)).thenReturn(Optional.of(es));

        service.removeSkillFromEmployee(1L, 1L);

        verify(esRepo).delete(es);
    }
}

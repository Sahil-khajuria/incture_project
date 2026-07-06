package com.erasm.controller;

import com.erasm.dto.request.EmployeeRequest;
import com.erasm.dto.response.EmployeeResponse;
import com.erasm.exception.EmployeeNotFoundException;
import com.erasm.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private EmployeeService employeeService;
    @MockBean private com.erasm.security.JwtTokenProvider jwtTokenProvider;
    @MockBean private com.erasm.security.CustomUserDetailsService customUserDetailsService;
    @MockBean private com.erasm.repository.BlacklistedTokenRepository blacklistedTokenRepository;

    private final EmployeeResponse sampleResp = EmployeeResponse.builder()
            .id(1L).userId(1L).email("john@erasm.com").firstName("John").lastName("Doe")
            .department("Engineering").benchStatus(true).skills(Collections.emptyList())
            .certifications(Collections.emptyList()).build();

    @Test @DisplayName("GET /employees - success 200")
    void getAllEmployees() throws Exception {
        Page<EmployeeResponse> page = new PageImpl<>(List.of(sampleResp), PageRequest.of(0, 20), 1);
        when(employeeService.getAllEmployees(any())).thenReturn(page);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].firstName").value("John"));
    }

    @Test @DisplayName("GET /employees/{id} - success 200")
    void getEmployeeById_Success() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(sampleResp);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.email").value("john@erasm.com"));
    }

    @Test @DisplayName("GET /employees/{id} - not found 404")
    void getEmployeeById_NotFound() throws Exception {
        when(employeeService.getEmployeeById(99L)).thenThrow(new EmployeeNotFoundException("Not found"));

        mockMvc.perform(get("/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("EMPLOYEE_NOT_FOUND"));
    }

    @Test @DisplayName("POST /employees - success 201")
    void createEmployee() throws Exception {
        EmployeeRequest req = EmployeeRequest.builder()
                .userId(1L).firstName("Jane").lastName("Smith").build();
        EmployeeResponse resp = EmployeeResponse.builder()
                .id(2L).firstName("Jane").lastName("Smith").build();
        when(employeeService.createEmployee(any())).thenReturn(resp);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.firstName").value("Jane"));
    }

    @Test @DisplayName("PUT /employees/{id} - success 200")
    void updateEmployee() throws Exception {
        EmployeeRequest req = EmployeeRequest.builder()
                .firstName("Updated").lastName("Name").build();
        EmployeeResponse resp = EmployeeResponse.builder()
                .id(1L).firstName("Updated").lastName("Name").build();
        when(employeeService.updateEmployee(eq(1L), any())).thenReturn(resp);

        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("Updated"));
    }

    @Test @DisplayName("DELETE /employees/{id} - success 200")
    void deleteEmployee() throws Exception {
        doNothing().when(employeeService).softDeleteEmployee(1L);

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Deleted"));
    }

    @Test @DisplayName("GET /employees/bench - success 200")
    void getBenchEmployees() throws Exception {
        Page<EmployeeResponse> page = new PageImpl<>(List.of(sampleResp), PageRequest.of(0, 20), 1);
        when(employeeService.getBenchEmployees(any())).thenReturn(page);

        mockMvc.perform(get("/employees/bench"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].benchStatus").value(true));
    }

    @Test @DisplayName("POST /employees - validation error on missing name")
    void createEmployee_ValidationError() throws Exception {
        String invalidJson = "{\"userId\":1,\"firstName\":\"\",\"lastName\":\"\"}";

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").exists());
    }
}

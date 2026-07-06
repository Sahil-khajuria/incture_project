package com.erasm.mapper;
import com.erasm.dto.request.EmployeeRequest; import com.erasm.dto.response.EmployeeResponse; import com.erasm.entity.*; import org.springframework.stereotype.Component; import java.time.LocalDate; import java.util.*; import java.util.stream.Collectors;

@Component public class EmployeeMapper {
    public EmployeeResponse toResponse(Employee e) {
        return EmployeeResponse.builder().id(e.getId()).userId(e.getUser().getId()).email(e.getUser().getEmail()).firstName(e.getFirstName()).lastName(e.getLastName()).phone(e.getPhone()).department(e.getDepartment()).designation(e.getDesignation()).dateOfJoining(e.getDateOfJoining()).employmentType(e.getEmploymentType()).benchStatus(e.getBenchStatus()).totalExperienceYears(e.getTotalExperienceYears())
            .skills(e.getSkills() == null ? Collections.emptyList() : e.getSkills().stream().map(es -> EmployeeResponse.EmployeeSkillResponse.builder().id(es.getId()).skillId(es.getSkill().getId()).skillName(es.getSkill().getName()).category(es.getSkill().getCategory()).level(es.getLevel().name()).yearsOfExperience(es.getYearsOfExperience()).lastUsedDate(es.getLastUsedDate()).build()).collect(Collectors.toList()))
            .certifications(e.getCertifications() == null ? Collections.emptyList() : e.getCertifications().stream().map(c -> EmployeeResponse.CertificationResponse.builder().id(c.getId()).name(c.getName()).issuingOrganization(c.getIssuingOrganization()).issueDate(c.getIssueDate()).expiryDate(c.getExpiryDate()).credentialId(c.getCredentialId()).credentialUrl(c.getCredentialUrl()).isExpired(c.getExpiryDate() != null && c.getExpiryDate().isBefore(LocalDate.now())).build()).collect(Collectors.toList()))
            .build();
    }
    public void updateEntity(Employee e, EmployeeRequest r) { if(r.getFirstName()!=null)e.setFirstName(r.getFirstName()); if(r.getLastName()!=null)e.setLastName(r.getLastName()); if(r.getPhone()!=null)e.setPhone(r.getPhone()); if(r.getDepartment()!=null)e.setDepartment(r.getDepartment()); if(r.getDesignation()!=null)e.setDesignation(r.getDesignation()); if(r.getDateOfJoining()!=null)e.setDateOfJoining(r.getDateOfJoining()); if(r.getEmploymentType()!=null)e.setEmploymentType(r.getEmploymentType()); if(r.getTotalExperienceYears()!=null)e.setTotalExperienceYears(r.getTotalExperienceYears()); }
}

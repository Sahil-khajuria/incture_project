package com.erasm.mapper;
import com.erasm.dto.response.ProjectResponse; import com.erasm.entity.Project; import com.erasm.enums.AllocationStatus; import org.springframework.stereotype.Component;
@Component public class ProjectMapper {
    public ProjectResponse toResponse(Project p) {
        int count = p.getAllocations() != null ? (int) p.getAllocations().stream().filter(a -> a.getStatus() == AllocationStatus.ACTIVE).count() : 0;
        return ProjectResponse.builder().id(p.getId()).name(p.getName()).clientName(p.getClientName()).description(p.getDescription()).startDate(p.getStartDate()).endDate(p.getEndDate()).status(p.getStatus().name()).technologyStack(p.getTechnologyStack()).budget(p.getBudget()).createdBy(p.getCreatedByUser() != null ? p.getCreatedByUser().getEmail() : null).allocatedEmployeeCount(count).build();
    }
}

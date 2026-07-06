package com.erasm.mapper;
import com.erasm.dto.response.AllocationResponse; import com.erasm.entity.Allocation; import org.springframework.stereotype.Component;
@Component public class AllocationMapper {
    public AllocationResponse toResponse(Allocation a) {
        return AllocationResponse.builder().id(a.getId()).employeeId(a.getEmployee().getId()).employeeName(a.getEmployee().getFirstName()+" "+a.getEmployee().getLastName()).projectId(a.getProject().getId()).projectName(a.getProject().getName()).resourceRequestId(a.getResourceRequest()!=null?a.getResourceRequest().getId():null).allocationPercentage(a.getAllocationPercentage()).startDate(a.getStartDate()).endDate(a.getEndDate()).status(a.getStatus().name()).allocatedBy(a.getAllocatedBy()!=null?a.getAllocatedBy().getEmail():null).build();
    }
}

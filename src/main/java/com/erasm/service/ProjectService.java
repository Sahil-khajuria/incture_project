package com.erasm.service;
import com.erasm.dto.request.ProjectRequest; import com.erasm.dto.response.*; import com.erasm.entity.*; import com.erasm.enums.*; import com.erasm.exception.*; import com.erasm.mapper.*; import com.erasm.repository.*; import com.erasm.util.AuditUtil; import org.slf4j.*; import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.List; import java.util.stream.Collectors;

@Service public class ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    private final ProjectRepository projRepo; private final AllocationRepository allocRepo; private final ResourceRequestRepository rrRepo; private final EmployeeRepository empRepo; private final UserRepository userRepo;
    private final ProjectMapper pMapper; private final AllocationMapper aMapper; private final AuditService audit; private final AuditUtil auditUtil;
    public ProjectService(ProjectRepository pr, AllocationRepository ar, ResourceRequestRepository rr, EmployeeRepository er, UserRepository ur, ProjectMapper pm, AllocationMapper am, AuditService as, AuditUtil au) { projRepo=pr; allocRepo=ar; rrRepo=rr; empRepo=er; userRepo=ur; pMapper=pm; aMapper=am; audit=as; auditUtil=au; }

    public Page<ProjectResponse> getAllProjects(Pageable p) { return projRepo.findAll(p).map(pMapper::toResponse); }
    public ProjectResponse getProjectById(Long id) { return pMapper.toResponse(projRepo.findById(id).orElseThrow(() -> new ProjectNotFoundException("Project not found: " + id))); }

    @Transactional public ProjectResponse createProject(ProjectRequest r, String email) { User u = userRepo.findByEmailIgnoreCase(email).orElse(null); Project p = projRepo.save(Project.builder().name(r.getName()).clientName(r.getClientName()).description(r.getDescription()).startDate(r.getStartDate()).endDate(r.getEndDate()).status(ProjectStatus.ACTIVE).technologyStack(r.getTechnologyStack()).budget(r.getBudget()).createdByUser(u).build()); audit.log("PROJECT",p.getId(),"CREATE",email,"Created: "+p.getName()); return pMapper.toResponse(p); }
    @Transactional public ProjectResponse updateProject(Long id, ProjectRequest r) { Project p = projRepo.findById(id).orElseThrow(() -> new ProjectNotFoundException("Not found")); if(r.getName()!=null)p.setName(r.getName()); if(r.getClientName()!=null)p.setClientName(r.getClientName()); if(r.getDescription()!=null)p.setDescription(r.getDescription()); if(r.getStartDate()!=null)p.setStartDate(r.getStartDate()); if(r.getEndDate()!=null)p.setEndDate(r.getEndDate()); if(r.getTechnologyStack()!=null)p.setTechnologyStack(r.getTechnologyStack()); if(r.getBudget()!=null)p.setBudget(r.getBudget()); p = projRepo.save(p); audit.log("PROJECT",id,"UPDATE",auditUtil.getCurrentUserEmail(),"Updated"); return pMapper.toResponse(p); }
    @Transactional public void softDeleteProject(Long id) { Project p = projRepo.findById(id).orElseThrow(() -> new ProjectNotFoundException("Not found")); if(!allocRepo.findByProjectIdAndStatus(id,AllocationStatus.ACTIVE).isEmpty()) throw new AllocationException("Cannot delete: active allocations exist"); p.setStatus(ProjectStatus.CANCELLED); projRepo.save(p); audit.log("PROJECT",id,"DELETE",auditUtil.getCurrentUserEmail(),"Soft-deleted"); }

    @Transactional public ProjectResponse closeProject(Long id) {
        Project p = projRepo.findById(id).orElseThrow(() -> new ProjectNotFoundException("Not found"));
        List<Allocation> active = allocRepo.findByProjectIdAndStatus(id, AllocationStatus.ACTIVE);
        for (Allocation a : active) { a.setStatus(AllocationStatus.COMPLETED); allocRepo.save(a); if (allocRepo.sumActiveAllocationPercentage(a.getEmployee().getId()) == 0) { a.getEmployee().setBenchStatus(true); empRepo.save(a.getEmployee()); } }
        rrRepo.findByProjectIdAndStatus(id, RequestStatus.ALLOCATED).forEach(rr -> { rr.setStatus(RequestStatus.COMPLETED); rrRepo.save(rr); });
        p.setStatus(ProjectStatus.COMPLETED); p = projRepo.save(p); audit.log("PROJECT",id,"UPDATE",auditUtil.getCurrentUserEmail(),"Closed"); logger.info("Project {} closed, {} allocations released", p.getName(), active.size()); return pMapper.toResponse(p);
    }
    public List<AllocationResponse> getProjectAllocations(Long pid) { projRepo.findById(pid).orElseThrow(() -> new ProjectNotFoundException("Not found")); return allocRepo.findByProjectId(pid).stream().map(aMapper::toResponse).collect(Collectors.toList()); }
}

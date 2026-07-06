package com.erasm.service;
import com.erasm.dto.request.AllocationRequest; import com.erasm.dto.response.*; import com.erasm.entity.*; import com.erasm.enums.*; import com.erasm.exception.*; import com.erasm.mapper.AllocationMapper; import com.erasm.repository.*; import com.erasm.util.AuditUtil; import org.slf4j.*; import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Isolation; import org.springframework.transaction.annotation.Transactional; import java.util.List; import java.util.stream.Collectors;

@Service public class AllocationService {
    private static final Logger logger = LoggerFactory.getLogger(AllocationService.class);
    private final AllocationRepository allocRepo; private final EmployeeRepository empRepo; private final ProjectRepository projRepo; private final ResourceRequestRepository rrRepo; private final UserRepository userRepo;
    private final AllocationMapper mapper; private final AuditService audit; private final AuditUtil auditUtil;
    public AllocationService(AllocationRepository ar, EmployeeRepository er, ProjectRepository pr, ResourceRequestRepository rr, UserRepository ur, AllocationMapper m, AuditService as, AuditUtil au) { allocRepo=ar; empRepo=er; projRepo=pr; rrRepo=rr; userRepo=ur; mapper=m; audit=as; auditUtil=au; }

    public Page<AllocationResponse> getAllAllocations(Pageable p) { return allocRepo.findAll(p).map(mapper::toResponse); }
    public AllocationResponse getAllocationById(Long id) { return mapper.toResponse(allocRepo.findById(id).orElseThrow(() -> new AllocationException("Not found: " + id))); }

    @Transactional(isolation=Isolation.SERIALIZABLE) public AllocationResponse allocate(AllocationRequest r, String email) {
        Employee e = empRepo.findById(r.getEmployeeId()).orElseThrow(()->new EmployeeNotFoundException("Not found")); if(!e.getUser().getIsActive()) throw new AllocationException("Inactive employee");
        Project p = projRepo.findById(r.getProjectId()).orElseThrow(()->new ProjectNotFoundException("Not found")); if(p.getStatus()!=ProjectStatus.ACTIVE) throw new AllocationException("Project not ACTIVE");
        if(r.getEndDate()!=null&&r.getStartDate().isAfter(r.getEndDate())) throw new AllocationException("Start after end");
        User allocator = userRepo.findByEmailIgnoreCase(email).orElse(null); if(allocator!=null&&e.getUser().getId().equals(allocator.getId())) throw new AllocationException("Self-allocation not allowed");
        int cur = allocRepo.sumActiveAllocationPercentage(r.getEmployeeId()); if(cur+r.getAllocationPercentage()>100) throw new AllocationException("Would exceed 100%. Current: "+cur+"%, Requested: "+r.getAllocationPercentage()+"%");
        Allocation a = Allocation.builder().employee(e).project(p).allocationPercentage(r.getAllocationPercentage()).startDate(r.getStartDate()).endDate(r.getEndDate()).status(AllocationStatus.ACTIVE).allocatedBy(allocator).build();
        if(r.getResourceRequestId()!=null) { ResourceRequest rr = rrRepo.findById(r.getResourceRequestId()).orElse(null); if(rr!=null) { a.setResourceRequest(rr); if(rr.getStatus()==RequestStatus.APPROVED) { rr.setStatus(RequestStatus.ALLOCATED); rrRepo.save(rr); } } }
        a = allocRepo.save(a); e.setBenchStatus(false); empRepo.save(e);
        audit.log("ALLOCATION",a.getId(),"ALLOCATE",email,"Employee "+e.getId()+" → Project "+p.getId()+" at "+r.getAllocationPercentage()+"%"); return mapper.toResponse(a);
    }

    @Transactional(isolation=Isolation.SERIALIZABLE) public AllocationResponse reallocate(Long id, AllocationRequest r) { Allocation a = allocRepo.findById(id).orElseThrow(()->new AllocationException("Not found")); if(a.getStatus()!=AllocationStatus.ACTIVE) throw new AllocationException("Not active"); int excl = allocRepo.sumActiveAllocationPercentageExcluding(a.getEmployee().getId(),id); if(excl+r.getAllocationPercentage()>100) throw new AllocationException("Would exceed 100%"); a.setAllocationPercentage(r.getAllocationPercentage()); if(r.getStartDate()!=null)a.setStartDate(r.getStartDate()); if(r.getEndDate()!=null)a.setEndDate(r.getEndDate()); a = allocRepo.save(a); audit.log("ALLOCATION",id,"UPDATE",auditUtil.getCurrentUserEmail(),"Reallocated"); return mapper.toResponse(a); }

    @Transactional public AllocationResponse release(Long id) { Allocation a = allocRepo.findById(id).orElseThrow(()->new AllocationException("Not found")); a.setStatus(AllocationStatus.RELEASED); allocRepo.save(a); if(allocRepo.sumActiveAllocationPercentage(a.getEmployee().getId())==0) { a.getEmployee().setBenchStatus(true); empRepo.save(a.getEmployee()); } audit.log("ALLOCATION",id,"RELEASE",auditUtil.getCurrentUserEmail(),"Released"); return mapper.toResponse(a); }

    public List<AllocationResponse> getEmployeeAllocations(Long eid) { empRepo.findById(eid).orElseThrow(()->new EmployeeNotFoundException("Not found")); return allocRepo.findByEmployeeId(eid).stream().map(mapper::toResponse).collect(Collectors.toList()); }

    public List<UtilizationResponse> getAllUtilization() { return empRepo.findAll().stream().map(this::buildUtil).collect(Collectors.toList()); }
    public UtilizationResponse getEmployeeUtilization(Long eid) { return buildUtil(empRepo.findById(eid).orElseThrow(()->new EmployeeNotFoundException("Not found"))); }
    private UtilizationResponse buildUtil(Employee e) { List<Allocation> active = allocRepo.findByEmployeeIdAndStatus(e.getId(),AllocationStatus.ACTIVE); int total = active.stream().mapToInt(Allocation::getAllocationPercentage).sum(); return UtilizationResponse.builder().employeeId(e.getId()).employeeName(e.getFirstName()+" "+e.getLastName()).totalAllocationPercentage(total).billablePercentage(total).benchPercentage(100-total).onBench(total==0).projects(active.stream().map(a->UtilizationResponse.ProjectAllocationSummary.builder().projectId(a.getProject().getId()).projectName(a.getProject().getName()).allocationPercentage(a.getAllocationPercentage()).build()).collect(Collectors.toList())).build(); }
}

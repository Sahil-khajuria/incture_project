package com.erasm.service;
import com.erasm.dto.request.ResourceRequestDto; import com.erasm.entity.*; import com.erasm.enums.RequestStatus; import com.erasm.exception.*; import com.erasm.repository.*; import com.erasm.util.AuditUtil; import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.time.LocalDateTime;

@Service public class ResourceRequestService {
    private final ResourceRequestRepository repo; private final ProjectRepository projRepo; private final SkillRepository skillRepo; private final UserRepository userRepo; private final AuditService audit; private final AuditUtil auditUtil;
    public ResourceRequestService(ResourceRequestRepository r, ProjectRepository p, SkillRepository s, UserRepository u, AuditService a, AuditUtil au) { repo=r; projRepo=p; skillRepo=s; userRepo=u; audit=a; auditUtil=au; }

    public Page<ResourceRequest> getAllRequests(RequestStatus status, Pageable p) { return status!=null ? repo.findByStatus(status,p) : repo.findAll(p); }
    public ResourceRequest getRequestById(Long id) { return repo.findById(id).orElseThrow(() -> new ResourceRequestNotFoundException("Not found: " + id)); }

    @Transactional public ResourceRequest createRequest(ResourceRequestDto d, String email) {
        Project p = projRepo.findById(d.getProjectId()).orElseThrow(() -> new ProjectNotFoundException("Not found"));
        Skill s = skillRepo.findById(d.getSkillId()).orElseThrow(() -> new SkillNotFoundException("Not found"));
        User u = userRepo.findByEmailIgnoreCase(email).orElseThrow(() -> new UserNotFoundException("Not found"));
        ResourceRequest rr = repo.save(ResourceRequest.builder().project(p).requestedBy(u).skill(s).skillLevel(d.getSkillLevel()).requiredCount(d.getRequiredCount()!=null?d.getRequiredCount():1).status(RequestStatus.DRAFT).remarks(d.getRemarks()).requestedStartDate(d.getRequestedStartDate()).requestedEndDate(d.getRequestedEndDate()).build());
        audit.log("RESOURCE_REQUEST",rr.getId(),"CREATE",email,"Created for project: "+p.getName()); return rr;
    }
    @Transactional public ResourceRequest updateRequest(Long id, ResourceRequestDto d) { ResourceRequest rr = getRequestById(id); if(rr.getStatus()!=RequestStatus.DRAFT) throw new InvalidStatusTransitionException("Can only update DRAFT"); if(d.getSkillId()!=null)rr.setSkill(skillRepo.findById(d.getSkillId()).orElseThrow(()->new SkillNotFoundException("Not found"))); if(d.getSkillLevel()!=null)rr.setSkillLevel(d.getSkillLevel()); if(d.getRequiredCount()!=null)rr.setRequiredCount(d.getRequiredCount()); if(d.getRemarks()!=null)rr.setRemarks(d.getRemarks()); if(d.getRequestedStartDate()!=null)rr.setRequestedStartDate(d.getRequestedStartDate()); if(d.getRequestedEndDate()!=null)rr.setRequestedEndDate(d.getRequestedEndDate()); return repo.save(rr); }
    @Transactional public void deleteRequest(Long id) { ResourceRequest rr = getRequestById(id); if(rr.getStatus()!=RequestStatus.DRAFT) throw new InvalidStatusTransitionException("Can only delete DRAFT"); repo.delete(rr); }
    @Transactional public ResourceRequest submitRequest(Long id) { ResourceRequest rr = getRequestById(id); validateTransition(rr.getStatus(),RequestStatus.SUBMITTED); rr.setStatus(RequestStatus.SUBMITTED); return repo.save(rr); }
    @Transactional public ResourceRequest approveRequest(Long id, String email) { ResourceRequest rr = getRequestById(id); if(rr.getStatus()==RequestStatus.SUBMITTED)rr.setStatus(RequestStatus.UNDER_REVIEW); validateTransition(rr.getStatus(),RequestStatus.APPROVED); rr.setStatus(RequestStatus.APPROVED); rr.setReviewedBy(userRepo.findByEmailIgnoreCase(email).orElse(null)); rr.setReviewedAt(LocalDateTime.now()); audit.log("RESOURCE_REQUEST",id,"APPROVE",email,"Approved"); return repo.save(rr); }
    @Transactional public ResourceRequest rejectRequest(Long id, String remarks, String email) { ResourceRequest rr = getRequestById(id); if(rr.getStatus()==RequestStatus.SUBMITTED)rr.setStatus(RequestStatus.UNDER_REVIEW); validateTransition(rr.getStatus(),RequestStatus.REJECTED); if(remarks==null||remarks.isBlank())throw new AllocationException("Remarks required"); rr.setStatus(RequestStatus.REJECTED); rr.setRemarks(remarks); rr.setReviewedBy(userRepo.findByEmailIgnoreCase(email).orElse(null)); rr.setReviewedAt(LocalDateTime.now()); audit.log("RESOURCE_REQUEST",id,"REJECT",email,"Rejected: "+remarks); return repo.save(rr); }

    private void validateTransition(RequestStatus cur, RequestStatus target) {
        boolean ok = switch(target) { case SUBMITTED->cur==RequestStatus.DRAFT; case UNDER_REVIEW->cur==RequestStatus.SUBMITTED; case APPROVED->cur==RequestStatus.UNDER_REVIEW; case REJECTED->cur==RequestStatus.UNDER_REVIEW; case ALLOCATED->cur==RequestStatus.APPROVED; case COMPLETED->cur==RequestStatus.ALLOCATED; case DRAFT->cur==RequestStatus.REJECTED; default->false; };
        if(!ok) throw new InvalidStatusTransitionException("Invalid: "+cur+" → "+target);
    }
}

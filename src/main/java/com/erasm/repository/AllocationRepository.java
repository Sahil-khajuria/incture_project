package com.erasm.repository;
import com.erasm.entity.Allocation; import com.erasm.enums.AllocationStatus; import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.data.jpa.repository.JpaRepository; import org.springframework.data.jpa.repository.Lock; import org.springframework.data.jpa.repository.Query; import org.springframework.data.repository.query.Param; import org.springframework.stereotype.Repository; import jakarta.persistence.LockModeType; import java.util.List;
@Repository public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    List<Allocation> findByEmployeeIdAndStatus(Long employeeId, AllocationStatus status);
    List<Allocation> findByProjectIdAndStatus(Long projectId, AllocationStatus status);
    List<Allocation> findByEmployeeId(Long employeeId);
    List<Allocation> findByProjectId(Long projectId);
    Page<Allocation> findByStatus(AllocationStatus status, Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("SELECT a FROM Allocation a WHERE a.employee.id = :eid AND a.status = 'ACTIVE'")
    List<Allocation> findActiveAllocationsForUpdate(@Param("eid") Long employeeId);
    @Query("SELECT COALESCE(SUM(a.allocationPercentage), 0) FROM Allocation a WHERE a.employee.id = :eid AND a.status = 'ACTIVE'")
    int sumActiveAllocationPercentage(@Param("eid") Long employeeId);
    @Query("SELECT COALESCE(SUM(a.allocationPercentage), 0) FROM Allocation a WHERE a.employee.id = :eid AND a.status = 'ACTIVE' AND a.id <> :exId")
    int sumActiveAllocationPercentageExcluding(@Param("eid") Long employeeId, @Param("exId") Long excludeId);
}

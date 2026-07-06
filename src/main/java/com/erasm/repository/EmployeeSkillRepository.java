package com.erasm.repository;
import com.erasm.entity.EmployeeSkill; import org.springframework.data.jpa.repository.JpaRepository; import org.springframework.stereotype.Repository; import java.util.List; import java.util.Optional;
@Repository public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Long> { List<EmployeeSkill> findByEmployeeId(Long employeeId); Optional<EmployeeSkill> findByEmployeeIdAndSkillId(Long employeeId, Long skillId); boolean existsByEmployeeIdAndSkillId(Long employeeId, Long skillId); List<EmployeeSkill> findBySkillId(Long skillId); }

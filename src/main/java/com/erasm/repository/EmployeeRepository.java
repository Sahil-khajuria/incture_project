package com.erasm.repository;
import com.erasm.entity.Employee; import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.data.jpa.repository.JpaRepository; import org.springframework.data.jpa.repository.Query; import org.springframework.data.repository.query.Param; import org.springframework.stereotype.Repository; import java.util.List; import java.util.Optional;
@Repository public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUserId(Long userId); Page<Employee> findByBenchStatusTrue(Pageable pageable); boolean existsByUserId(Long userId);
    @Query("SELECT e FROM Employee e JOIN e.skills es WHERE es.skill.name = :skillName AND es.level = :level AND e.benchStatus = true")
    List<Employee> findBySkillAndLevelAndAvailable(@Param("skillName") String skillName, @Param("level") String level);
}

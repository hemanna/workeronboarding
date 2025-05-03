package com.sbd.common.repository;

import com.sbd.common.entity.EmployeeSkills;
import com.sbd.common.entity.Skill;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeSkillsRepository implements PanacheRepository<EmployeeSkills> {
    public boolean existsByEmployeeAndSkill(Integer employeeId, Integer skillId) {
        Long count = count("employee.id = ?1 and skill.id = ?2", employeeId, skillId);
        return count > 0;
    }

}

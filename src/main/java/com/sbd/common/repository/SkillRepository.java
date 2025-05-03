package com.sbd.common.repository;

import com.sbd.common.entity.Role;
import com.sbd.common.entity.Skill;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SkillRepository implements PanacheRepository<Skill> {
    public Skill findBySkillId(int skillId) {
        return find("Id", skillId).firstResult();
    }

}

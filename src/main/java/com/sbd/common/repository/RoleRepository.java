package com.sbd.common.repository;

import com.sbd.common.entity.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {


    public Role findByRoleId(int roleId) {
        return find("roleId", roleId).firstResult();
    }
}

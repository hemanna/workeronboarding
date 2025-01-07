package com.sbd.common.repository;

import com.sbd.common.entity.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {

    // Custom query to find role by its name
    public Role findByRoleName(String roleName) {
        return find("roleName", roleName).firstResult();
    }
}

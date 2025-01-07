package com.sbd.common.repository;

import com.sbd.common.entity.Department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepository<Department> {

    // Custom query to find department by name
    public Department findByName(String name) {
        return find("name", name).firstResult();
    }
}

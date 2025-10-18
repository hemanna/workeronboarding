package com.sbd.common.repository;

import com.sbd.common.entity.Department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepository<Department> {
    public Department findById(int id) {
        return find("id", id).firstResult();
    }
}

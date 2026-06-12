package com.sbd.common.repository;

import com.sbd.common.entity.Department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepository<Department> {
    public Department findById(int id) {
        return find("id", id).firstResult();

    }
    public List<Department> listAllDepartments() {
        return find("ORDER BY id DESC").list();
    }
}

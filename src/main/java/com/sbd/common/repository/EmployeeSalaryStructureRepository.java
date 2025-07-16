package com.sbd.common.repository;

import com.sbd.common.entity.BankDetails;
import com.sbd.common.entity.EmployeeSalaryStructure;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeSalaryStructureRepository implements PanacheRepository<EmployeeSalaryStructure> {
    public EmployeeSalaryStructure findByEmployeeId(Integer employeeId) {
        return find("employee.id", employeeId).firstResult();
    }
}


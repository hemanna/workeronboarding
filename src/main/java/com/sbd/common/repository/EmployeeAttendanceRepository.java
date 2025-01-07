package com.sbd.common.repository;

import com.sbd.common.entity.EmployeeAttendance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeAttendanceRepository implements PanacheRepository<EmployeeAttendance> {

    // Custom query to find attendance by employeeId
    public EmployeeAttendance findByEmployeeId(Long employeeId) {
        return find("employee.id", employeeId).firstResult();
    }
}

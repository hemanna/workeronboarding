package com.sbd.common.repository;

import com.sbd.common.entity.Leave;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LeaveRepository implements PanacheRepository<Leave> {

    // Custom query to find leave by employeeId
    public Leave findByEmployeeId(Long employeeId) {
        return find("employee.id", employeeId).firstResult();
    }
}

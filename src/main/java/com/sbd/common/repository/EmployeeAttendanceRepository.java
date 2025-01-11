package com.sbd.common.repository;

import com.sbd.common.entity.EmployeeAttendance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;

@ApplicationScoped
public class EmployeeAttendanceRepository implements PanacheRepository<EmployeeAttendance> {


    public EmployeeAttendance findByEmployeeAndDate(int employeeId, LocalDate date) {
        return find("employee.id = ?1 and date = ?2", employeeId, date).firstResult();
    }

}

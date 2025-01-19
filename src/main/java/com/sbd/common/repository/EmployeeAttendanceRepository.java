package com.sbd.common.repository;

import com.sbd.common.entity.EmployeeAttendance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class EmployeeAttendanceRepository implements PanacheRepository<EmployeeAttendance> {


    public EmployeeAttendance findByEmployeeAndDate(int employeeId, LocalDate date) {
        return find("employee.id = ?1 and date = ?2", employeeId, date).firstResult();
    }

    // New method to find all attendance by date
    public List<EmployeeAttendance> findByDate(LocalDate date) {
        return list("date = ?1", date);
    }

    // Method to fetch attendance by month
    public List<EmployeeAttendance> findByMonth(LocalDate startDate, LocalDate endDate) {
        return list("date >= ?1 and date <= ?2", startDate, endDate);
    }

}

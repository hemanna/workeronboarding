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

    // find all attendance by date
    public List<EmployeeAttendance> findByDate(LocalDate date) {
        return list("date = ?1", date);
    }

    // fetch attendance by month
    public List<EmployeeAttendance> findByMonth(LocalDate startDate, LocalDate endDate) {
        return list("date >= ?1 and date <= ?2", startDate, endDate);
    }
    public List<EmployeeAttendance> findByDateRange(LocalDate fromDate, LocalDate toDate) {
        return find("date >= ?1 AND date <= ?2", fromDate, toDate).list();
    }

}

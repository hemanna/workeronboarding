package com.sbd.common.repository;

import com.sbd.common.entity.EmployeeAttendance;
import com.sbd.common.entity.EmployeeAttendanceSession;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class EmployeeAttendanceSessionRepository implements PanacheRepository<EmployeeAttendanceSession> {
    public List<EmployeeAttendanceSession> findByAttendanceId(Integer attendanceId) {
        return list("attendance.id", attendanceId);
    }

}

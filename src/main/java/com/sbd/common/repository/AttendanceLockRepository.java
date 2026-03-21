package com.sbd.common.repository;

import com.sbd.common.entity.AttendanceLock;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ApplicationScoped
public class AttendanceLockRepository implements PanacheRepository<AttendanceLock> {

    @Inject
    EntityManager em;

    //Check attendance exists
    public Long countAttendance(Integer month, Integer year) {
        return (Long) em.createQuery(QueryEnum.COUNT_ATTENDANCE.getValue())
                .setParameter("month", month)
                .setParameter("year", year)
                .getSingleResult();
    }

    // Check already locked
    public Long countLocked(Integer month, Integer year) {
        return (Long) em.createQuery(QueryEnum.COUNT_LOCKED.getValue())
                .setParameter("month", month)
                .setParameter("year", year)
                .getSingleResult();
    }

    // Insert lock
    public int insertLock(Integer month, Integer year) {
        return em.createNativeQuery(QueryEnum.INSERT_LOCK.getValue())
                .setParameter(1, month)
                .setParameter(2, year)
                .executeUpdate();
    }

    //Update attendance status
    public int updateAttendanceStatus(Integer month, Integer year) {
        return em.createQuery(QueryEnum.UPDATE_ATTENDANCE_STATUS.getValue())
                .setParameter("month", month)
                .setParameter("year", year)
                .executeUpdate();
    }

    @Getter
    @AllArgsConstructor
    public enum QueryEnum {

        COUNT_ATTENDANCE(
                "SELECT COUNT(a) FROM EmployeeAttendance a " +
                        "WHERE FUNCTION('MONTH', a.date) = :month " +
                        "AND FUNCTION('YEAR', a.date) = :year"
        ),

        COUNT_LOCKED(
                "SELECT COUNT(al) FROM AttendanceLock al " +
                        "WHERE al.month = :month " +
                        "AND al.year = :year " +
                        "AND al.locked = true"
        ),

        INSERT_LOCK(
                "INSERT INTO attendance_lock (employee_id, month, year, locked, locked_at) " +
                        "SELECT DISTINCT a.employee_id, ?1, ?2, TRUE, NOW() " +
                        "FROM employee_attendance a " +
                        "WHERE MONTH(a.date) = ?1 " +
                        "AND YEAR(a.date) = ?2 " +
                        "AND NOT EXISTS ( " +
                        "    SELECT 1 FROM attendance_lock al " +
                        "    WHERE al.employee_id = a.employee_id " +
                        "    AND al.month = ?1 " +
                        "    AND al.year = ?2 " +
                        ")"
        ),

        UPDATE_ATTENDANCE_STATUS(
                "UPDATE EmployeeAttendance a " +
                        "SET a.approvalStatus = 'APPROVED' " +
                        "WHERE FUNCTION('MONTH', a.date) = :month " +
                        "AND FUNCTION('YEAR', a.date) = :year"
        );

        private final String value;
    }
}
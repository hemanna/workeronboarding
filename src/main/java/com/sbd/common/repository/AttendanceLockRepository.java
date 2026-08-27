package com.sbd.common.repository;

import com.sbd.common.entity.AttendanceLock;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@ApplicationScoped
public class AttendanceLockRepository implements PanacheRepository<AttendanceLock> {

    @Inject
    EntityManager em;

    // Check attendance exists for selected month/year
    public Long countAttendance(
            Integer month,
            Integer year
    ) {
        return em.createQuery(
                        QueryEnum.COUNT_ATTENDANCE.getValue(),
                        Long.class
                )
                .setParameter("month", month)
                .setParameter("year", year)
                .getSingleResult();
    }


    // Get locked employee IDs for selected month/year
    public List<Integer> findLockedEmployeeIds(
            Integer month,
            Integer year
    ) {
        return em.createQuery(
                        QueryEnum.FIND_LOCKED_EMPLOYEE_IDS.getValue(),
                        Integer.class
                )
                .setParameter("month", month)
                .setParameter("year", year)
                .getResultList();
    }


    // Insert lock for selected employee
    public int insertLock(
            Integer employeeId,
            Integer month,
            Integer year
    ) {
        return em.createNativeQuery(
                        QueryEnum.INSERT_LOCK.getValue()
                )
                .setParameter(1, employeeId)
                .setParameter(2, month)
                .setParameter(3, year)
                .executeUpdate();
    }


    // Update attendance status only for selected employee
    public int updateAttendanceStatus(
            Integer employeeId,
            Integer month,
            Integer year
    ) {
        return em.createQuery(
                        QueryEnum.UPDATE_ATTENDANCE_STATUS.getValue()
                )
                .setParameter("employeeId", employeeId)
                .setParameter("month", month)
                .setParameter("year", year)
                .executeUpdate();
    }

    // Check whether employee is already locked for selected month/year
    public Long countEmployeeLocked(
            Integer employeeId,
            Integer month,
            Integer year
    ) {
        return em.createQuery(
                        QueryEnum.COUNT_EMPLOYEE_LOCKED.getValue(),
                        Long.class
                )
                .setParameter("employeeId", employeeId)
                .setParameter("month", month)
                .setParameter("year", year)
                .getSingleResult();
    }

    @Getter
    @AllArgsConstructor
    public enum QueryEnum {

        COUNT_ATTENDANCE(
                "SELECT COUNT(a) " +
                        "FROM EmployeeAttendance a " +
                        "WHERE FUNCTION('MONTH', a.date) = :month " +
                        "AND FUNCTION('YEAR', a.date) = :year"
        ),

        FIND_LOCKED_EMPLOYEE_IDS(
                "SELECT al.employee.id " +
                        "FROM AttendanceLock al " +
                        "WHERE al.month = :month " +
                        "AND al.year = :year " +
                        "AND al.locked = true " +
                        "ORDER BY al.employee.id"
        ),


        INSERT_LOCK(
                "INSERT INTO attendance_lock " +
                        "(employee_id, month, year, locked, locked_at) " +
                        "VALUES (?1, ?2, ?3, TRUE, NOW())"
        ),

        COUNT_EMPLOYEE_LOCKED(
                "SELECT COUNT(al) " +
                        "FROM AttendanceLock al " +
                        "WHERE al.employee.id = :employeeId " +
                        "AND al.month = :month " +
                        "AND al.year = :year " +
                        "AND al.locked = true"
        ),
        UPDATE_ATTENDANCE_STATUS(
                "UPDATE EmployeeAttendance a " +
                        "SET a.approvalStatus = 'APPROVED' " +
                        "WHERE a.employee.id = :employeeId " +
                        "AND FUNCTION('MONTH', a.date) = :month " +
                        "AND FUNCTION('YEAR', a.date) = :year"
        );


        private final String value;
    }
}
package com.sbd.common.repository;

import com.sbd.common.entity.EmployeeAttendance;
import com.sbd.common.entity.EmployeeDetails;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class EmployeeAttendanceRepository implements PanacheRepository<EmployeeAttendance> {

    @Inject
    EntityManager em;


    public EmployeeAttendance findById(Integer employeeId) {
        return find("id", employeeId).firstResult();
    }

    public EmployeeAttendance findByEmployeeAndDate(int employeeId, LocalDate date) {
        return find("employee.id = ?1 and date = ?2", employeeId, date).firstResult();
    }

    public EmployeeAttendance findByEmployeeAndDate(
            Integer employeeId,
            LocalDate date
    ) {

        return find(
                QueryEnum.FIND_BY_EMPLOYEE_AND_DATE.getValue(),
                employeeId,
                date
        ).firstResult();
    }

    // CALCULATE TOTAL WORKING HOURS
    public Double calculateWorkingHours(Integer attendanceId) {

        Object result = em.createNativeQuery(
                        QueryEnum.CALCULATE_WORKING_HOURS.getValue()
                )
                .setParameter(1, attendanceId)
                .getSingleResult();

        return result != null
                ? ((Number) result).doubleValue()
                : 0.0;
    }

    // UPDATE employee_attendance TABLE
    public int updateWorkingHours(
            Integer attendanceId,
            Double hours
    ) {

        return update(
                QueryEnum.UPDATE_WORKING_HOURS.getValue(),
                BigDecimal.valueOf(hours),
                attendanceId
        );
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


    public Optional<Map<String, Object>> getAttendanceForCurrentMonth(Long employeeId) {
        return getEntityManager()
                .createQuery(QueryEnum.GET_ATTENDANCE_FOR_CURRENT_MONTH.getValue(),
                        (Class<Map<String, Object>>) (Class<?>) Map.class)
                .setParameter(QueryEnum.EMPLOYEE_ID.getValue(), employeeId)
                .getResultStream()
                .findFirst();
    }

    public List<EmployeeAttendance> listAllAttendance(
            int pageIndex, int pageSize) {

        return find(QueryEnum.QUERY_LIST_ALL.getValue())
                .page(pageIndex, pageSize)
                .list();
    }

    public List<EmployeeAttendance> listOvertimeForCurrentMonth(
            int pageIndex,
            int pageSize
    ) {
        return find(QueryEnum.QUERY_LIST_OVERTIME_CURRENT_MONTH.getValue())
                .page(pageIndex, pageSize)
                .list();
    }



    @Getter
    @AllArgsConstructor
    private enum QueryEnum {
        GET_ATTENDANCE_FOR_CURRENT_MONTH("SELECT new map( " +
                "e.employee.id AS employeeId, " +
                "COUNT(CASE WHEN e.status = 'Present' THEN 1 END) AS presentDays, " +
                "COUNT(CASE WHEN e.status = 'Pending' THEN 1 END) AS pendingDays, " +
                "COUNT(*) AS totalRecords, " +
                "FUNCTION('DAY', FUNCTION('LAST_DAY', CURRENT_DATE)) AS totalDaysInMonth, " +
                "SUM(CASE WHEN FUNCTION('DAYOFWEEK', e.date) NOT IN (1, 7) THEN 1 ELSE 0 END) AS totalWorkingDays) " +
                "FROM EmployeeAttendance e " +
                "WHERE e.employee.id = :employeeId " +
                "AND FUNCTION('YEAR', e.date) = FUNCTION('YEAR', CURRENT_DATE) " +
                "AND FUNCTION('MONTH', e.date) = FUNCTION('MONTH', CURRENT_DATE) " +
                "GROUP BY e.employee.id"),
        QUERY_LIST_ALL(
                "SELECT ea FROM EmployeeAttendance ea ORDER BY ea.date DESC"
        ),
        QUERY_LIST_OVERTIME_CURRENT_MONTH(
                "SELECT ea FROM EmployeeAttendance ea " +
                        "WHERE ea.overtime IS NOT NULL " +
                        "AND ea.overtime > 0 " +
                        "AND FUNCTION('MONTH', ea.date) = FUNCTION('MONTH', CURRENT_DATE) " +
                        "AND FUNCTION('YEAR', ea.date) = FUNCTION('YEAR', CURRENT_DATE) " +
                        "ORDER BY ea.date DESC"
        ),
        FIND_BY_EMPLOYEE_AND_DATE(
                "employee.id = ?1 and date = ?2"
        ),

        CALCULATE_WORKING_HOURS(
                "SELECT " +
                        "IFNULL(SUM(TIMESTAMPDIFF(MINUTE, check_in, check_out)),0) / 60 " +
                        "FROM employee_attendance_sessions " +
                        "WHERE attendance_id = ?1 " +
                        "AND check_in IS NOT NULL " +
                        "AND check_out IS NOT NULL"
        ),

        UPDATE_WORKING_HOURS(
                "workingHours = ?1 where id = ?2"
        ),

        EMPLOYEE_ID("employeeId");

        private final String value;
    }
}

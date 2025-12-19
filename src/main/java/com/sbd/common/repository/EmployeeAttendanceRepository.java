package com.sbd.common.repository;

import com.sbd.common.entity.EmployeeAttendance;
import com.sbd.common.entity.EmployeeDetails;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class EmployeeAttendanceRepository implements PanacheRepository<EmployeeAttendance> {

    public EmployeeAttendance findById(Integer employeeId) {
        return find("id", employeeId).firstResult();
    }

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


        EMPLOYEE_ID("employeeId");

        private final String value;
    }
}

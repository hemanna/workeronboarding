package com.sbd.common.repository;

import com.sbd.common.Jsonb.AttendanceTodayJsonb;
import com.sbd.common.entity.EmployeeAttendance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public void saveAttendance(EmployeeAttendance attendance) {
        persist(attendance);
    }

    public Long getPresentToday(LocalDate date) {

        return count(
                QueryEnum.GET_PRESENT_TODAY.getValue(),
                date
        );
    }

    public Long getAbsentToday(LocalDate date) {

        return count(
                QueryEnum.GET_ABSENT_TODAY.getValue(),
                date
        );
    }

    public Long getTimeShortage(LocalDate date) {

        return count(
                QueryEnum.GET_TIME_SHORTAGE.getValue(),
                date
        );
    }

    public Double getAttendancePercentage(LocalDate date) {

        Object result = getEntityManager()
                .createQuery(
                        QueryEnum.GET_ATTENDANCE_PERCENTAGE.getValue()
                )
                .setParameter(1, date)
                .getSingleResult();

        return result == null ? 0.0 : ((Number) result).doubleValue();
    }

    public Long getLateArrivals(LocalDate date) {

        Object result = em.createNativeQuery(
                        QueryEnum.GET_LATE_ARRIVALS.getValue())
                .setParameter(1, date)
                .getSingleResult();

        return result == null ? 0L : ((Number) result).longValue();
    }

    public List<Object[]> fetchMonthlyAttendanceTrend(
            Integer year,
            Integer month
    ) {

        return em.createNativeQuery(
                        QueryEnum.MONTHLY_ATTENDANCE_TREND.getValue()
                )
                .setParameter(1, year)
                .setParameter(2, month)
                .getResultList();
    }

    public Long getPresentCount(LocalDate date) {

        return count(
                QueryEnum.GET_PRESENT_COUNT.getValue(),
                date
        );
    }

    public Long getAbsentCount(LocalDate date) {

        return count(
                QueryEnum.GET_ABSENT_COUNT.getValue(),
                date
        );
    }

    public List<AttendanceTodayJsonb> fetchTodayAttendance() {

        List<Object[]> rows = em.createNativeQuery(
                QueryEnum.GET_TODAY_ATTENDANCE.getValue()
        ).getResultList();

        List<AttendanceTodayJsonb> response = new ArrayList<>();

        for (Object[] row : rows) {

            LocalTime checkIn = null;
            LocalTime checkOut = null;

            if (row[3] != null) {
                checkIn = ((Time) row[3]).toLocalTime();
            }

            if (row[4] != null) {
                checkOut = ((Time) row[4]).toLocalTime();
            }

            response.add(
                    new AttendanceTodayJsonb(
                            ((Number) row[0]).intValue(),
                            row[1] == null ? "" : row[1].toString(),
                            row[2] == null ? "" : row[2].toString(),
                            checkIn,
                            checkOut,
                            (BigDecimal) row[5]
                    )
            );
        }

        return response;
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
        GET_PRESENT_TODAY(
                "date=?1 AND status='PRESENT'"
        ),

        GET_ABSENT_TODAY(
                "date=?1 AND status='ABSENT'"
        ),

        GET_TIME_SHORTAGE(
                "date=?1 AND workingHours < 8"
        ),

        GET_ATTENDANCE_PERCENTAGE(

                "SELECT " +
                        "(SUM(CASE WHEN e.status='PRESENT' THEN 1 ELSE 0 END)*100.0)/COUNT(e) " +
                        "FROM EmployeeAttendance e " +
                        "WHERE e.date=?1"

        ),

        GET_LATE_ARRIVALS(
                "SELECT COUNT(*) " +
                        "FROM employee_attendance_sessions s " +
                        "JOIN employee_attendance a ON s.attendance_id = a.id " +
                        "WHERE a.date = ?1 " +
                        "AND s.check_in > '09:30:00'"
        ),
        MONTHLY_ATTENDANCE_TREND(

                "SELECT " +
                        "DAY(date) day, " +
                        "ROUND( " +
                        "(SUM(CASE WHEN status='PRESENT' THEN 1 ELSE 0 END) *100.0) / COUNT(*),2 " +
                        ") attendancePercentage " +
                        "FROM employee_attendance " +
                        "WHERE YEAR(date)=?1 " +
                        "AND MONTH(date)=?2 " +
                        "GROUP BY DAY(date) " +
                        "ORDER BY DAY(date)"

        ),GET_PRESENT_COUNT(

                "date=?1 AND status='PRESENT'"

        ),

        GET_ABSENT_COUNT(

                "date=?1 AND status='ABSENT'"

        ),
        GET_TODAY_ATTENDANCE(

                "SELECT " +
                        "ed.id, " +
                        "ed.employee_name, " +
                        "d.name, " +
                        "ea.check_in, " +
                        "ea.check_out, " +
                        "ea.working_hours " +
                        "FROM employee_attendance ea " +
                        "INNER JOIN employee_details ed ON ea.employee_id = ed.id " +
                        "INNER JOIN department d ON ea.department_id = d.id " +
                        "WHERE ea.date = CURDATE() " +
                        "ORDER BY ed.employee_name"

        ),


        EMPLOYEE_ID("employeeId");

        private final String value;
    }
}

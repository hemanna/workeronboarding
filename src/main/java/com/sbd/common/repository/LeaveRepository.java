package com.sbd.common.repository;

import com.sbd.common.Jsonb.LeaveDTO;
import com.sbd.common.Jsonb.LeaveStatusDistributionJsonb;
import com.sbd.common.Jsonb.LeaveSummaryJsonb;
import com.sbd.common.Jsonb.LeaveTrendJsonb;
import com.sbd.common.entity.Leave;
import com.sbd.common.mapper.LeaveMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class LeaveRepository implements PanacheRepository<Leave> {

    @Inject
    EntityManager em;

    public Leave findById(Integer id) {
        return find("id", id).firstResult();
    }

    public List<LeaveDTO> findAllWithLeaveDays() {
        List<Leave> leaves = getEntityManager().createQuery(
                        "SELECT l FROM Leave l", Leave.class)
                .getResultList();

        return leaves.stream()
                .map(LeaveMapper.INSTANCE::toDTO)
                .peek(l -> l.setNumberOfDays(ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 0))
                .collect(Collectors.toList());
    }

    public List<Object[]> findLeaveBalancesByEmployeeId(int employeeId, int year) {
        return getEntityManager()
                .createNativeQuery(LeaveQueryEnum.LEAVE_BALANCE.getValue())
                .setParameter(LeaveQueryEnum.EMPLOYEE_ID.getValue(), employeeId)
                .setParameter(LeaveQueryEnum.YEAR.getValue(), year)
                .getResultList();
    }

    public LeaveSummaryJsonb fetchLeaveSummary() {

        Object[] row = (Object[]) em.createNativeQuery(
                LeaveQueryEnum.GET_LEAVE_SUMMARY.getValue()
        ).getSingleResult();

        LeaveSummaryJsonb response = new LeaveSummaryJsonb();

        response.setTotalLeaveRequests(
                ((Number) row[0]).longValue());

        response.setApprovedLeaves(
                ((Number) row[1]).longValue());

        response.setPendingLeaves(
                ((Number) row[2]).longValue());

        response.setRejectedLeaves(
                ((Number) row[3]).longValue());

        response.setRegularization(
                ((Number) row[4]).longValue());

        return response;
    }

    public List<LeaveStatusDistributionJsonb> fetchLeaveStatusDistribution() {

        List<Object[]> rows =
                em.createNativeQuery(
                        LeaveQueryEnum.GET_LEAVE_STATUS_DISTRIBUTION.getValue()
                ).getResultList();

        List<LeaveStatusDistributionJsonb> response =
                new ArrayList<>();

        for (Object[] row : rows) {

            response.add(

                    new LeaveStatusDistributionJsonb(

                            row[0].toString(),

                            ((Number) row[1]).longValue()

                    )
            );
        }

        return response;
    }

    public List<LeaveTrendJsonb> fetchLast10DaysLeaveTrend() {

        List<Object[]> rows =
                em.createNativeQuery(
                        LeaveQueryEnum.GET_LAST_10_DAYS_TREND.getValue()
                ).getResultList();

        return rows.stream()
                .map(row -> new LeaveTrendJsonb(

                        row[0].toString(),

                        ((Number) row[1]).longValue()

                ))
                .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    private enum LeaveQueryEnum {
        LEAVE_BALANCE(
                "SELECT lt.id, lt.type, lt.annual_entitlement, " +
                        "COALESCE(SUM(DATEDIFF(lr.end_date, lr.start_date) + 1), 0) AS used " +
                        "FROM leave_type lt " +
                        "LEFT JOIN leave_requests lr ON lr.leave_type_id = lt.id " +
                        "AND lr.employee_id = :employeeId " +
                        "AND YEAR(lr.start_date) = :year " +
                        "GROUP BY lt.id, lt.type, lt.annual_entitlement"
        ),
        GET_LEAVE_SUMMARY(

                "SELECT " +

                        "COUNT(*) totalLeaveRequests, " +

                        "SUM(CASE WHEN status='APPROVED' THEN 1 ELSE 0 END), " +

                        "SUM(CASE WHEN status='PENDING' THEN 1 ELSE 0 END), " +

                        "SUM(CASE WHEN status='REJECTED' THEN 1 ELSE 0 END), " +

                        "0 " +

                        "FROM leave_requests"

        ),

        GET_LEAVE_STATUS_DISTRIBUTION(

                "SELECT " +
                        "lt.type, " +
                        "COUNT(l.id) " +
                        "FROM leave_requests l " +
                        "INNER JOIN leave_type lt " +
                        "ON l.leave_type_id = lt.id " +
                        "GROUP BY lt.type " +
                        "ORDER BY COUNT(l.id) DESC"

        ),
        GET_LAST_10_DAYS_TREND(

                "SELECT " +

                        "DATE(applied_date) AS leaveDate, " +

                        "COUNT(*) AS total " +

                        "FROM leave_requests " +

                        "WHERE applied_date >= DATE_SUB(CURDATE(), INTERVAL 9 DAY) " +

                        "GROUP BY DATE(applied_date) " +

                        "ORDER BY leaveDate"

        ),

        EMPLOYEE_ID("employeeId"),
        YEAR("year");

        private final String value;
    }


}

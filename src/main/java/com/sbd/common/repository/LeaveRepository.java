package com.sbd.common.repository;

import com.sbd.common.Jsonb.LeaveDTO;
import com.sbd.common.entity.Leave;
import com.sbd.common.mapper.LeaveMapper;
import com.sbd.common.request.EmployeeDTO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class LeaveRepository implements PanacheRepository<Leave> {

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


        EMPLOYEE_ID("employeeId"),
        YEAR("year");

        private final String value;
    }


}

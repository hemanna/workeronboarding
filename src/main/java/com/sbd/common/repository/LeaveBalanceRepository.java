package com.sbd.common.repository;

import com.sbd.common.Jsonb.EmployeeLeaveBalanceJsonb;
import com.sbd.common.entity.LeaveBalance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LeaveBalanceRepository implements PanacheRepository<LeaveBalance> {
    @Inject
    EntityManager em;

    public List<LeaveBalance> findByEmployeeIdAndYear(Integer employeeId, String year) {
        return
                find("employee.id = ?1 AND leavePeriod = ?2", employeeId, year).list();
    }

    public LeaveBalance findByEmployeeAndLeaveTypeAndYear(Integer employeeId, Integer leaveTypeId, String year) {
        return find("employee.id = ?1 AND leaveType.id = ?2 AND leavePeriod = ?3", employeeId, leaveTypeId, year).firstResult();
    }

    public List<EmployeeLeaveBalanceJsonb> fetchEmployeeLeaveBalance() {

        List<Object[]> rows =
                em.createNativeQuery(
                        QueryEnum.GET_EMPLOYEE_LEAVE_BALANCE.getValue()
                ).getResultList();

        List<EmployeeLeaveBalanceJsonb> response =
                new ArrayList<>();

        for (Object[] row : rows) {

            response.add(

                    new EmployeeLeaveBalanceJsonb(

                            row[0].toString(),

                            row[1].toString(),

                            ((Number) row[2]).intValue(),

                            ((Number) row[3]).intValue(),

                            ((Number) row[4]).intValue(),

                            ((Number) row[5]).intValue()

                    )
            );
        }

        return response;
    }

    @Getter
    @AllArgsConstructor
    private enum QueryEnum {

        FIND_BY_EMPLOYEE_AND_YEAR(

                "employee.id = ?1 AND leavePeriod = ?2"

        ),

        FIND_BY_EMPLOYEE_LEAVE_TYPE_YEAR(

                "employee.id = ?1 AND leaveType.id = ?2 AND leavePeriod = ?3"

        ),

        GET_EMPLOYEE_LEAVE_BALANCE(

                "SELECT " +
                        "e.employee_name, " +
                        "lb.leave_period, " +
                        "SUM(CASE " +
                        "WHEN lt.type = 'Casual Leave' " +
                        "THEN lb.remaining_days ELSE 0 END) AS casualLeave, " +

                        "SUM(CASE " +
                        "WHEN lt.type = 'Sick Leave' " +
                        "THEN lb.remaining_days ELSE 0 END) AS sickLeave, " +

                        "SUM(lb.taken_days) AS usedLeaves, " +

                        "SUM(lb.remaining_days) AS remainingLeaves " +

                        "FROM leave_balance lb " +

                        "INNER JOIN employee_details e " +
                        "ON lb.employee_id = e.id " +

                        "INNER JOIN leave_type lt " +
                        "ON lb.leave_type_id = lt.id " +

                        "GROUP BY " +
                        "e.employee_name, " +
                        "lb.leave_period " +

                        "ORDER BY e.employee_name"

        );

        private final String value;
    }

}

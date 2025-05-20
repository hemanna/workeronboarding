package com.sbd.common.repository;

import com.sbd.common.entity.LeaveBalance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@ApplicationScoped
public class LeaveBalanceRepository implements PanacheRepository<LeaveBalance> {
    public List<LeaveBalance> findByEmployeeIdAndYear(Integer employeeId, String year) {
        return
                find("employee.id = ?1 AND leavePeriod = ?2", employeeId, year).list();
    }

    public LeaveBalance findByEmployeeAndLeaveTypeAndYear(Integer employeeId, Integer leaveTypeId, String year) {
        return find("employee.id = ?1 AND leaveType.id = ?2 AND leavePeriod = ?3", employeeId, leaveTypeId, year).firstResult();
    }

}

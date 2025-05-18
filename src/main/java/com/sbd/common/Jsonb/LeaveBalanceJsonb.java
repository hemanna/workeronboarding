package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveBalanceJsonb {
    private int employeeId;
    private int leaveTypeId;
    private int entitled;
    private int takenDays;
    private int carryForwardDays;
    private int remainingDays;
    private String leavePeriod;
    private boolean isCarryForwardAllowed;
}

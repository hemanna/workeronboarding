package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLeaveBalanceJsonb {
    private String employeeName;

    private String month;

    private Integer casualLeave;

    private Integer sickLeave;

    private Integer used;

    private Integer remaining;
}

package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyPayrollJsonb {
    private Integer employeeId;

    private String employeeName;

    private BigDecimal basic;

    private BigDecimal ot;

    private BigDecimal bonus;

    private BigDecimal lop;

    private BigDecimal gross;

    private BigDecimal deductions;

    private BigDecimal net;

    private String status;

}

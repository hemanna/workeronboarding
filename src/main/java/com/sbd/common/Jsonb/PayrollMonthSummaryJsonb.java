package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayrollMonthSummaryJsonb {
    private Integer totalEmployees;
    private BigDecimal totalPayrollCost;
    private BigDecimal totalDeductions;
}

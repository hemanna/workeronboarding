package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class SalaryDashboardDTO {
    private BigDecimal totalGrossPay;
    private BigDecimal totalDeduction;
    private BigDecimal totalNetPay;
    private Long employeeCount;

}

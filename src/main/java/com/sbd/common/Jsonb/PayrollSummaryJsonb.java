package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollSummaryJsonb {
    private BigDecimal totalGross;

    private BigDecimal totalDeductions;

    private BigDecimal totalNetPay;
}

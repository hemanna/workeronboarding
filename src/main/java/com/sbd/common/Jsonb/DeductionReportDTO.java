package com.sbd.common.Jsonb;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeductionReportDTO {
    private String name;

    private BigDecimal pf;
    private BigDecimal esi;
    private BigDecimal pt;
    private BigDecimal lop;

    private BigDecimal advance;
    private BigDecimal other;

    private BigDecimal totalDeduction;


}

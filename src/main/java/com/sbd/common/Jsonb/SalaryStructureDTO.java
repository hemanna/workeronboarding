package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryStructureDTO {
    private String componentName;
    private BigDecimal amount;
    private String type;
}

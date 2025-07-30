package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollDTO {
    private BigDecimal grossSalary;
    private BigDecimal netSalary;
    private LocalDate generatedOn;
    private Integer month;
    private Integer year;
    private List<EmployeeSalaryStructureJsonb> components;

}

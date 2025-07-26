package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePayslipDTO {
    private Long employeeId;
    private String employeeName;
    private List<EmployeeSalaryStructureJsonb> salaryStructures;

}

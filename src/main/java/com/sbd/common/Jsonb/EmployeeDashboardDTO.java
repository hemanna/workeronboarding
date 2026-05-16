package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDashboardDTO {
    private Long totalEmployees;

    private Long approvedEmployees;

    private Long activeEmployees;

    private Long inactiveEmployees;
}

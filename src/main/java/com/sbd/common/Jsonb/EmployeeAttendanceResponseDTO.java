package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAttendanceResponseDTO {
    private Long employeeId;
    private Integer presentDays;
    private Integer pendingDays;
    private Integer totalRecords;
    private Integer totalDaysInMonth;
    private Integer totalWorkingDays;

}

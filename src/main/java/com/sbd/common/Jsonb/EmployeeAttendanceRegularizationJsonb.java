package com.sbd.common.Jsonb;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAttendanceRegularizationJsonb {
    private Integer attendanceId;
    private Integer employeeId;
    private LocalDate date;
    private String currentStatus;
    private LocalTime newCheckin;
    private LocalTime newCheckout;
    private String newLocation;
    private String reason;

}

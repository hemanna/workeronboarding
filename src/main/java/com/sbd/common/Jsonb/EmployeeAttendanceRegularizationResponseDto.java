package com.sbd.common.Jsonb;

import com.sbd.common.request.EmployeeDTO;
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
public class EmployeeAttendanceRegularizationResponseDto {
    private Integer id;               // Regularization ID
    private Integer attendanceId;
    private Integer employeeId;       // Only ID, no nested employee details
    private String status;            // Pending / Approved / Rejected
    private LocalDate date;
    private String currentStatus;
    private LocalTime newCheckin;
    private LocalTime newCheckout;
    private String newLocation;
    private String reason;

}

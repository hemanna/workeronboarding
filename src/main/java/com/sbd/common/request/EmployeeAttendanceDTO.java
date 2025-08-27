package com.sbd.common.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAttendanceDTO {
    private Integer id;
    private Integer employeeId;
    private Integer departmentId;
    private Integer roleId;
    private LocalDate date;
    private LocalTime checkinTime;
    private LocalTime checkoutTime;
    private BigDecimal workingHours;
    private BigDecimal overtime;
    private String shiftDetails;
    private String location;
    private String photo;
    private String approvalStatus;
    private String status;
    private Integer leaveId;
}

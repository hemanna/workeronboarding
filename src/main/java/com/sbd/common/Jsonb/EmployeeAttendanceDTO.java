package com.sbd.common.Jsonb;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAttendanceDTO {
    private Integer id;
    private Integer employeeId;
    private Integer departmentId;
    private Integer roleId;
    private LocalDate date;
    private BigDecimal workingHours;
    private BigDecimal overtime;
    private String shiftDetails;
    private String location;
    private String photo;
    private String approvalStatus;
    private String status;
    private Integer leaveId;
    private List<EmployeeAttendanceSessionDTO> sessions;

}

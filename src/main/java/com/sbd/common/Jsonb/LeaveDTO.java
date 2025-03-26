package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDTO {
    private Integer id;
    private Integer employeeId;
    private Integer leaveTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer departmentId;
    private String reason;
    private LocalDate appliedDate;
    private String status;
    private String adminRemarks;
    private String attachment;
    private String attachmentName;
    private long numberOfDays;


}

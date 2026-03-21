package com.sbd.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmployeeAttendanceActionEnum {
    OVERTIME_LIST("Overtime Attendance List"),
    ATTENDANCE_LOCK("Employee Attendance Lock"),
    Attendance_LIST("Attendance List");

    private final String value;
}

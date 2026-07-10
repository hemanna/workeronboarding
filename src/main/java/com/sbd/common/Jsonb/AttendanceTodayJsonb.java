package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceTodayJsonb {
    private Integer employeeId;

    private String employeeName;

    private String departmentName;

    private LocalTime checkIn;

    private LocalTime checkOut;

    private BigDecimal workingHours;

}

package com.sbd.common.Jsonb;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class AttendanceMarkingJsonb {

    private Integer employeeId;
    private String status; // PRESENT, HALF_DAY, ABSENT
    private LocalTime checkIn;
    private LocalTime checkOut;
    private BigDecimal otHours;
}

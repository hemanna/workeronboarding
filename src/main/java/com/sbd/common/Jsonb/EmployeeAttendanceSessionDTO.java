package com.sbd.common.Jsonb;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAttendanceSessionDTO {
    private LocalTime checkIn;
    private LocalTime checkOut;
    private String location;

}

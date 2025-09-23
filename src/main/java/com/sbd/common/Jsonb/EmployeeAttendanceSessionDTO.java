package com.sbd.common.Jsonb;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // ignore nulls in JSON output
public class EmployeeAttendanceSessionDTO {
    private LocalTime checkIn;
    private LocalTime checkOut;
    private String location;

}

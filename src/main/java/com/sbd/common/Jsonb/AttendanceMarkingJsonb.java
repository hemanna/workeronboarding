package com.sbd.common.Jsonb;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AttendanceMarkingJsonb {

    private Integer employeeId;
    private String status; // PRESENT, HALF_DAY, ABSENT

}

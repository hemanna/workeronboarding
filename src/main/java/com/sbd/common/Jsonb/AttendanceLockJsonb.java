package com.sbd.common.Jsonb;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttendanceLockJsonb {
    private Integer month;
    private Integer year;
    private List<Integer> employeeIds;

}

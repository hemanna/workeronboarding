package com.sbd.common.Jsonb;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class AttendanceSubmitJsonb {
    private List<AttendanceMarkingJsonb> attendances;

}

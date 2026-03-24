package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollStatusJsonb {
    private String month;
    private String attendanceStatus;
    private String payrollStatus;
}

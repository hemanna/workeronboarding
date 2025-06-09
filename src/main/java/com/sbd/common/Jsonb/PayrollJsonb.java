package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayrollJsonb {
    private Long employeeId;
    private Integer month;
    private Integer year;
}

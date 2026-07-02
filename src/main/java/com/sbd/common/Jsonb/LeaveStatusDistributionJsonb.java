package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveStatusDistributionJsonb {
    private String leaveType;

    private Long count;
}

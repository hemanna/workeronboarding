package com.sbd.globalsetting.common.jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveSettingsJsonb {
    private Integer id;

    private Integer annualLeaveDays;

    private Integer sickLeaveDays;

    private Integer casualLeaveDays;

    private Integer maximumCarryForward;

    private String approvalLevel;

    private Boolean lossOfPayAllowed;

    private Boolean carryForwardLeave;

    private Boolean allowLeaveCancellation;
}

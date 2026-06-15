package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeDTO {
    private Integer id;
    private String type;
    private Integer annualEntitlement;
    private Boolean isCarryForwardAllowed;

}

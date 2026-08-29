package com.sbd.globalsetting.common.jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvancedSettingsJsonb {

    private Integer id;

    private String employeeIdPrefix;

    private String attendancePrefix;

    private String leavePrefix;

    private String salaryPrefix;

    private Boolean enableAuditLogs;

    private Boolean developerMode;
}

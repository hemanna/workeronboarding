package com.sbd.globalsetting.common.jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSettingsJsonb {

    private Integer id;

    private Boolean emailNotifications;

    private Boolean smsNotifications;

    private Boolean pushNotifications;

    private Boolean attendanceAlerts;

    private Boolean leaveApprovalAlerts;

    private Boolean salaryAlerts;
}

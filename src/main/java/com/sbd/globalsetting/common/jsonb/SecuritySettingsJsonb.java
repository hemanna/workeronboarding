package com.sbd.globalsetting.common.jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecuritySettingsJsonb {

    private Integer id;

    private Integer sessionTimeoutMinutes;

    private Integer passwordExpiryDays;

    private Integer minimumPasswordLength;

    private Boolean requireSpecialCharacters;

    private Boolean enableOtpLogin;

    private Boolean allowMultipleDeviceLogin;
}
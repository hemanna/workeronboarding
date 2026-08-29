package com.sbd.globalsetting.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecuritySettingsActionEnum {

    INSERT_SECURITY_SETTINGS(
            "INSERT SECURITY SETTINGS"
    );

    private final String value;
}

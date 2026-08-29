package com.sbd.globalsetting.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationSettingsActionEnum {

    INSERT_NOTIFICATION_SETTINGS(
            "INSERT NOTIFICATION SETTINGS"
    );

    private final String value;
}

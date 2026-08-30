package com.sbd.globalsetting.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BackupSettingsActionEnum {

    INSERT_BACKUP_SETTINGS("INSERT BACKUP SETTINGS"),

    RUN_BACKUP_NOW("RUN BACKUP NOW");

    private final String value;
}

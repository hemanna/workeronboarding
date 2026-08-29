package com.sbd.globalsetting.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeneralSettingsActionEnum {
    FETCH_GENERAL_SETTINGS("FETCH GENERAL SETTINGS"),

    INSERT_GENERAL_SETTINGS("SAVE GENERAL SETTINGS"),

    CREATE_GENERAL_SETTINGS("CREATE GENERAL SETTINGS"),

    UPDATE_GENERAL_SETTINGS("UPDATE GENERAL SETTINGS");


    private final String value;

}

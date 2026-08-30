package com.sbd.globalsetting.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyHolidayActionEnum {

    INSERT_HOLIDAY("INSERT HOLIDAY"),

    GET_HOLIDAYS("GET HOLIDAYS"),

    IMPORT_HOLIDAYS("IMPORT HOLIDAYS"),

    EXPORT_HOLIDAYS("EXPORT HOLIDAYS");

    private final String value;
}

package com.sbd.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SalaryActionEnum {
    SALARY_REPORT("Salary report Detail"),
    SALARY_DASHBOARD("Salary Detail");

    private final String value;
}

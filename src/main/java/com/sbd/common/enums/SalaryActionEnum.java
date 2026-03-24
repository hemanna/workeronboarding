package com.sbd.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SalaryActionEnum {
    SALARY_REPORT("Salary report Detail"),
    DEDUCTION_REPORT("Deduction report Detail"),
    APPROVED_ATTENDANCE("FETCH APPROVED ATTENDANCE STATUS"),
    SALARY_DASHBOARD("Salary Detail");

    private final String value;
}

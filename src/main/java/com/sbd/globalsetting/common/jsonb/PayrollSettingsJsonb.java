package com.sbd.globalsetting.common.jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollSettingsJsonb {

    private Integer id;

    private String salaryCycle;

    private String salaryCalculation;

    private String payslipPassword;

    private Boolean enablePf;

    private Boolean enableEsi;

    private Boolean enableProfessionalTax;

    private Boolean autoGeneratePayslip;
}

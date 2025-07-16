package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSalaryStructureJsonb {
    private Integer salaryStructureId;
    private Integer employeeId;
    private Integer departmentId;
    private String location;

    private BigDecimal basicSalary;
    private BigDecimal houseRentAllowance;
    private BigDecimal specialAllowance;
    private BigDecimal npsEmployer;
    private BigDecimal carReimbursement;
    private BigDecimal driverReimbursement;
    private BigDecimal pdReimbursement;
    private BigDecimal telephoneReimbursement;

    private BigDecimal grossSalary;
    private BigDecimal pfContribution;
    private BigDecimal esiContribution;
    private BigDecimal fixedSalary;
    private BigDecimal gratuityPayable;
    private BigDecimal bonusPayable;
    private BigDecimal ltaPayable;
    private BigDecimal variablePayable;
    private BigDecimal mediclaimBenefits;
    private BigDecimal grandTotalCtc;

    private BigDecimal monthlySalary;
    private BigDecimal annualCtc;
    private String approvalStatus;
    private String salaryStatus;

    private Boolean pfApplicable;
    private Boolean pfLimit;
    private Boolean esiApplicable;
    private Boolean gratuityApplicable;
    private Boolean bonusApplicable;
    private BigDecimal basicSalaryPercent;
    private BigDecimal hraPercent;
    private BigDecimal npsPercent;
    private BigDecimal minimumWage;
}

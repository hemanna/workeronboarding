package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_salary_structure")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeSalaryStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeDetails employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    private String location;

    // Fixed Components
    @Column(name = "basic_salary")
    private BigDecimal basicSalary;

    @Column(name = "house_rent_allowance")
    private BigDecimal houseRentAllowance;

    @Column(name = "special_allowance")
    private BigDecimal specialAllowance;

    @Column(name = "nps_employer")
    private BigDecimal npsEmployer;

    @Column(name = "car_reimbursement")
    private BigDecimal carReimbursement;

    @Column(name = "driver_reimbursement")
    private BigDecimal driverReimbursement;

    @Column(name = "pd_reimbursement")
    private BigDecimal pdReimbursement;

    @Column(name = "telephone_reimbursement")
    private BigDecimal telephoneReimbursement;

    // Salary Calculations
    @Column(name = "gross_salary")
    private BigDecimal grossSalary;

    @Column(name = "pf_contribution")
    private BigDecimal pfContribution;

    @Column(name = "esi_contribution")
    private BigDecimal esiContribution;

    @Column(name = "fixed_salary")
    private BigDecimal fixedSalary;

    @Column(name = "gratuity_payable")
    private BigDecimal gratuityPayable;

    @Column(name = "bonus_payable")
    private BigDecimal bonusPayable;

    @Column(name = "lta_payable")
    private BigDecimal ltaPayable;

    @Column(name = "variable_payable")
    private BigDecimal variablePayable;

    @Column(name = "mediclaim_benefits")
    private BigDecimal mediclaimBenefits;

    @Column(name = "grand_total_ctc")
    private BigDecimal grandTotalCtc;

    // Summary Fields
    @Column(name = "monthly_salary")
    private BigDecimal monthlySalary;

    @Column(name = "annual_ctc")
    private BigDecimal annualCtc;

    @Column(name = "approval_status")
    private String approvalStatus;

    @Column(name = "salary_status")
    private String salaryStatus;

    // Percentages & Settings
    @Column(name = "pf_applicable")
    private Boolean pfApplicable;

    @Column(name = "pf_limit")
    private Boolean pfLimit;

    @Column(name = "esi_applicable")
    private Boolean esiApplicable;

    @Column(name = "gratuity_applicable")
    private Boolean gratuityApplicable;

    @Column(name = "bonus_applicable")
    private Boolean bonusApplicable;

    @Column(name = "basic_salary_percent")
    private BigDecimal basicSalaryPercent;

    @Column(name = "hra_percent")
    private BigDecimal hraPercent;

    @Column(name = "nps_percent")
    private BigDecimal npsPercent;

    @Column(name = "minimum_wage")
    private BigDecimal minimumWage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}

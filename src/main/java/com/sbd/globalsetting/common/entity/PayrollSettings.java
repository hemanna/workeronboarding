package com.sbd.globalsetting.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payroll_settings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PayrollSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "salary_cycle", nullable = false, length = 50)
    private String salaryCycle;

    @Column(name = "salary_calculation", nullable = false, length = 50)
    private String salaryCalculation;

    @Column(name = "payslip_password", nullable = false, length = 50)
    private String payslipPassword;

    @Column(name = "enable_pf", nullable = false)
    private Byte  enablePf;

    @Column(name = "enable_esi", nullable = false)
    private Byte  enableEsi;

    @Column(name = "enable_professional_tax", nullable = false)
    private Byte  enableProfessionalTax;

    @Column(name = "auto_generate_payslip", nullable = false)
    private Byte  autoGeneratePayslip;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

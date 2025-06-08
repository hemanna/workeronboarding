package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payroll_components")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PayrollComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_id")
    private Integer componentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_id")
    private Payroll payrollId;

    @Column(name = "component_name")
    private String componentName;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "type")
    private String type;

}

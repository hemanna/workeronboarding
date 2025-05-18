package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leave_balance")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeDetails employee;

    @ManyToOne
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

    @Column(name = "taken_days")
    private Integer takenDays;

    @Column(name = "remaining_days")
    private Integer remainingDays;

    @Column(name = "carry_forward_days")
    private Integer carryForwardDays;

    @Column(name = "leave_period")
    private String leavePeriod;
}

package com.sbd.globalsetting.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "leave_settings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LeaveSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "annual_leave_days", nullable = false)
    private Integer annualLeaveDays;

    @Column(name = "sick_leave_days", nullable = false)
    private Integer sickLeaveDays;

    @Column(name = "casual_leave_days", nullable = false)
    private Integer casualLeaveDays;

    @Column(name = "maximum_carry_forward", nullable = false)
    private Integer maximumCarryForward;

    @Column(name = "approval_level", length = 50)
    private String approvalLevel;

    @Column(name = "loss_of_pay_allowed", nullable = false)
    private Byte  lossOfPayAllowed;

    @Column(name = "carry_forward_leave", nullable = false)
    private Byte  carryForwardLeave;

    @Column(name = "allow_leave_cancellation", nullable = false)
    private Byte  allowLeaveCancellation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

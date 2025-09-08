package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "employee_attendance_regularization")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EmployeeAttendanceRegularization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // FK to employee_attendance
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reg_attendance"))
    private EmployeeAttendance attendance;

    // FK to employee_details
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reg_employee"))
    private EmployeeDetails employee;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "current_status", nullable = false, length = 20)
    private String currentStatus; // e.g., Absent/Present/Late

    @Column(name = "new_checkin")
    private LocalTime newCheckin;

    @Column(name = "new_checkout")
    private LocalTime newCheckout;

    @Column(name = "new_location", length = 255)
    private String newLocation;

    @Column(name = "reason", nullable = false, length = 255)
    private String reason;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING"; // Pending / Approved / Rejected

    @Column(name = "approved_by")
    private Integer approvedBy; // Manager userId

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

}

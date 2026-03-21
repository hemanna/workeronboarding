package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;


@Entity
@Table(name = "attendance_lock")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttendanceLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeDetails employee;

    @Column(nullable = false)
    private Boolean locked = false;

    @Column(name = "locked_by")
    private Integer lockedBy;

    @Column(name = "locked_at")
    private Timestamp lockedAt;

    private String remarks;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

}

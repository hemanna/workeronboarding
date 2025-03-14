package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "leave_requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id",  nullable = false)
    private EmployeeDetails employee;

    @ManyToOne
    @JoinColumn(name = "leave_type_id",   nullable = false)
    private LeaveType leaveType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "reason")
    private String reason;

    @Lob
    @Column(name = "attachment")
    private byte[] attachment;

    @Column(name = "attachment_name")
    private String attachmentName;

    @Column(name = "status")
    private String status;

    @Column(name = "applied_date")
    private LocalDate appliedDate;

    @Column(name = "admin_remarks")
    private String adminRemarks;
}

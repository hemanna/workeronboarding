package com.sbd.globalsetting.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "advanced_settings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdvancedSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "employee_id_prefix", nullable = false, length = 20)
    private String employeeIdPrefix;

    @Column(name = "attendance_prefix", nullable = false, length = 20)
    private String attendancePrefix;

    @Column(name = "leave_prefix", nullable = false, length = 20)
    private String leavePrefix;

    @Column(name = "salary_prefix", nullable = false, length = 20)
    private String salaryPrefix;

    @Column(name = "enable_audit_logs", nullable = false)
    private Byte  enableAuditLogs;

    @Column(name = "developer_mode", nullable = false)
    private Byte developerMode;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

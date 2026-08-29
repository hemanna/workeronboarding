package com.sbd.globalsetting.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_settings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NotificationSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email_notifications", nullable = false)
    private Byte  emailNotifications;

    @Column(name = "sms_notifications", nullable = false)
    private Byte  smsNotifications;

    @Column(name = "push_notifications", nullable = false)
    private Byte  pushNotifications;

    @Column(name = "attendance_alerts", nullable = false)
    private Byte  attendanceAlerts;

    @Column(name = "leave_approval_alerts", nullable = false)
    private Byte  leaveApprovalAlerts;

    @Column(name = "salary_alerts", nullable = false)
    private Byte  salaryAlerts;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

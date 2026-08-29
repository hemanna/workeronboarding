package com.sbd.globalsetting.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "security_settings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SecuritySettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "session_timeout_minutes", nullable = false)
    private Integer sessionTimeoutMinutes;

    @Column(name = "password_expiry_days", nullable = false)
    private Integer passwordExpiryDays;

    @Column(name = "minimum_password_length", nullable = false)
    private Integer minimumPasswordLength;

    @Column(name = "require_special_characters", nullable = false)
    private Byte  requireSpecialCharacters;

    @Column(name = "enable_otp_login", nullable = false)
    private Byte  enableOtpLogin;

    @Column(name = "allow_multiple_device_login", nullable = false)
    private Byte  allowMultipleDeviceLogin;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

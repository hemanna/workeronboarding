package com.sbd.globalsetting.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_settings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttendanceSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "working_hours_per_day", nullable = false, precision = 4, scale = 2)
    private BigDecimal workingHoursPerDay;

    @Column(name = "grace_time_minutes", nullable = false)
    private Integer graceTimeMinutes;

    @Column(name = "late_mark_after_minutes", nullable = false)
    private Integer lateMarkAfterMinutes;

    @Column(name = "maximum_working_hours", nullable = false, precision = 4, scale = 2)
    private BigDecimal maximumWorkingHours;

    @Column(name = "minimum_half_day_hours", nullable = false, precision = 4, scale = 2)
    private BigDecimal minimumHalfDayHours;

    @Column(name = "minimum_full_day_hours", nullable = false, precision = 4, scale = 2)
    private BigDecimal minimumFullDayHours;

    @Column(name = "allow_multiple_check_in", nullable = false)
    private Byte  allowMultipleCheckIn;

    @Column(name = "allow_multiple_check_out", nullable = false)
    private Byte  allowMultipleCheckOut;

    @Column(name = "enable_gps_validation", nullable = false)
    private Byte  enableGpsValidation;

    @Column(name = "enable_photo_capture", nullable = false)
    private Byte  enablePhotoCapture;

    @Column(name = "allow_manual_attendance", nullable = false)
    private Byte  allowManualAttendance;

    @Column(name = "require_manager_approval", nullable = false)
    private Byte  requireManagerApproval;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

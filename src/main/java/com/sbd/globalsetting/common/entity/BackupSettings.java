package com.sbd.globalsetting.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "backup_settings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BackupSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "automatic_backup", nullable = false)
    private Byte  automaticBackup;

    @Column(name = "backup_time", nullable = false)
    private LocalTime backupTime;

    @Column(name = "retention_period_days", nullable = false)
    private Integer retentionPeriodDays;

    @Column(name = "backup_location", nullable = false, length = 100)
    private String backupLocation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

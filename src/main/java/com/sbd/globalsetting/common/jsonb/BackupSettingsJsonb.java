package com.sbd.globalsetting.common.jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackupSettingsJsonb {

    private Integer id;

    private Boolean automaticBackup;

    private LocalTime backupTime;

    private Integer retentionPeriodDays;

    private String backupLocation;
}

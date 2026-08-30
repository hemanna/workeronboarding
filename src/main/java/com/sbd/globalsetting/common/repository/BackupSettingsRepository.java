package com.sbd.globalsetting.common.repository;

import com.sbd.globalsetting.common.entity.AttendanceSettings;
import com.sbd.globalsetting.common.entity.BackupSettings;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BackupSettingsRepository implements PanacheRepository<BackupSettings> {
}

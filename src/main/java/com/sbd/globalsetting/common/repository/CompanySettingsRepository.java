package com.sbd.globalsetting.common.repository;

import com.sbd.globalsetting.common.entity.AttendanceSettings;
import com.sbd.globalsetting.common.entity.CompanySettings;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CompanySettingsRepository implements PanacheRepository<CompanySettings> {
}

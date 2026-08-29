package com.sbd.globalsetting.common.repository;

import com.sbd.globalsetting.common.entity.PayrollSettings;
import com.sbd.globalsetting.common.entity.SecuritySettings;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SecuritySettingsRepository implements PanacheRepository<SecuritySettings> {
}

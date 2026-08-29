package com.sbd.globalsetting.common.repository;

import com.sbd.globalsetting.common.entity.AdvancedSettings;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AdvancedSettingsRepository implements PanacheRepository<AdvancedSettings> {
}

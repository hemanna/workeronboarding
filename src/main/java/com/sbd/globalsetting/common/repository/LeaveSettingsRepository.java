package com.sbd.globalsetting.common.repository;

import com.sbd.globalsetting.common.entity.GeneralSettings;
import com.sbd.globalsetting.common.entity.LeaveSettings;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LeaveSettingsRepository implements PanacheRepository<LeaveSettings> {
}

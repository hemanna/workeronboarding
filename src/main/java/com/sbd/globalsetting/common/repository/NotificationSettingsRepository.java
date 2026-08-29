package com.sbd.globalsetting.common.repository;

import com.sbd.globalsetting.common.entity.LeaveSettings;
import com.sbd.globalsetting.common.entity.NotificationSettings;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationSettingsRepository implements PanacheRepository<NotificationSettings> {
}

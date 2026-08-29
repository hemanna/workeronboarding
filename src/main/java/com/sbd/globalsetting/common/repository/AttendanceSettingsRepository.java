package com.sbd.globalsetting.common.repository;

import com.sbd.globalsetting.common.entity.AttendanceSettings;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ApplicationScoped
public class AttendanceSettingsRepository implements PanacheRepository<AttendanceSettings> {
}

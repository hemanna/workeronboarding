package com.sbd.globalsetting.common.repository;

import com.sbd.globalsetting.common.entity.GeneralSettings;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ApplicationScoped
public class GeneralSettingsRepository
        implements PanacheRepository<GeneralSettings> {

    @Inject
    EntityManager em;

    public GeneralSettings insertGeneralSettings(
            GeneralSettings generalSettings
    ) {

        persist(generalSettings);

        flush();

        return generalSettings;
    }

    public long countGeneralSettings() {
        return count();
    }

    @Getter
    @AllArgsConstructor
    public enum QueryEnum {

        COMPANY_NAME("companyName"),

        COMPANY_CODE("companyCode"),

        ORGANIZATION_TYPE("organizationType"),

        CURRENCY("currency"),

        TIME_ZONE("timeZone"),

        LANGUAGE("language"),

        DATE_FORMAT("dateFormat"),

        TIME_FORMAT("timeFormat"),

        QUERY_LIST_ALL(
                "SELECT g FROM GeneralSettings g ORDER BY g.id DESC"
        );

        private final String value;
    }

}

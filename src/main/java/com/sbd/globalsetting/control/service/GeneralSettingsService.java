package com.sbd.globalsetting.control.service;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.globalsetting.common.entity.GeneralSettings;
import com.sbd.globalsetting.common.enums.GeneralSettingsActionEnum;
import com.sbd.globalsetting.common.jsonb.GeneralSettingsJsonb;
import com.sbd.globalsetting.common.repository.GeneralSettingsRepository;
import com.sbd.globalsetting.control.GeneralSettingsControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@ApplicationScoped
public class GeneralSettingsService implements GeneralSettingsControl {
    @Inject
    GeneralSettingsRepository generalSettingsRepository;

    @Override
    @Transactional
    public ApiResponse insertGeneralSettings(
            GeneralSettingsJsonb generalSettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                GeneralSettingsActionEnum.INSERT_GENERAL_SETTINGS.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        GeneralSettings generalSettings = new GeneralSettings();

        generalSettings.setCompanyName(
                generalSettingsJsonb.getCompanyName()
        );

        generalSettings.setCompanyCode(
                generalSettingsJsonb.getCompanyCode()
        );

        generalSettings.setOrganizationType(
                generalSettingsJsonb.getOrganizationType()
        );

        generalSettings.setCurrency(
                generalSettingsJsonb.getCurrency()
        );

        generalSettings.setTimeZone(
                generalSettingsJsonb.getTimeZone()
        );

        generalSettings.setLanguage(
                generalSettingsJsonb.getLanguage()
        );

        generalSettings.setDateFormat(
                generalSettingsJsonb.getDateFormat()
        );

        generalSettings.setTimeFormat(
                generalSettingsJsonb.getTimeFormat()
        );

        generalSettings.setCreatedAt(
                LocalDateTime.now()
        );

        generalSettings.setUpdatedAt(
                LocalDateTime.now()
        );

        generalSettingsRepository.persist(generalSettings);

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                GeneralSettingsActionEnum.INSERT_GENERAL_SETTINGS.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                )
        );
    }
}

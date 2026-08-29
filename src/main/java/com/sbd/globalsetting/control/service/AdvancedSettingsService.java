package com.sbd.globalsetting.control.service;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.globalsetting.common.entity.AdvancedSettings;
import com.sbd.globalsetting.common.enums.AdvancedSettingsActionEnum;
import com.sbd.globalsetting.common.jsonb.AdvancedSettingsJsonb;
import com.sbd.globalsetting.common.repository.AdvancedSettingsRepository;
import com.sbd.globalsetting.control.AdvancedSettingsControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@ApplicationScoped
public class AdvancedSettingsService
        implements AdvancedSettingsControl {

    @Inject
    AdvancedSettingsRepository advancedSettingsRepository;

    @Override
    @Transactional
    public ApiResponse insertAdvancedSettings(
            AdvancedSettingsJsonb advancedSettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AdvancedSettingsActionEnum
                        .INSERT_ADVANCED_SETTINGS
                        .getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        AdvancedSettings advancedSettings =
                new AdvancedSettings();

        advancedSettings.setEmployeeIdPrefix(
                advancedSettingsJsonb.getEmployeeIdPrefix()
        );

        advancedSettings.setAttendancePrefix(
                advancedSettingsJsonb.getAttendancePrefix()
        );

        advancedSettings.setLeavePrefix(
                advancedSettingsJsonb.getLeavePrefix()
        );

        advancedSettings.setSalaryPrefix(
                advancedSettingsJsonb.getSalaryPrefix()
        );

        advancedSettings.setEnableAuditLogs(
                booleanToByte(
                        advancedSettingsJsonb.getEnableAuditLogs()
                )
        );

        advancedSettings.setDeveloperMode(
                booleanToByte(
                        advancedSettingsJsonb.getDeveloperMode()
                )
        );

        advancedSettings.setCreatedAt(
                LocalDateTime.now()
        );

        advancedSettings.setUpdatedAt(
                LocalDateTime.now()
        );

        advancedSettingsRepository.persist(
                advancedSettings
        );

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AdvancedSettingsActionEnum
                        .INSERT_ADVANCED_SETTINGS
                        .getValue(),
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

    private Byte booleanToByte(Boolean value) {

        return Boolean.TRUE.equals(value)
                ? (byte) 1
                : (byte) 0;
    }
}
package com.sbd.record.control.service;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.globalsetting.common.entity.BackupSettings;
import com.sbd.globalsetting.common.enums.BackupSettingsActionEnum;
import com.sbd.globalsetting.common.jsonb.BackupSettingsJsonb;
import com.sbd.globalsetting.common.repository.BackupSettingsRepository;
import com.sbd.record.control.BackupSettingsControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@ApplicationScoped
public class BackupSettingsService
        implements BackupSettingsControl {

    @Inject
    BackupSettingsRepository backupSettingsRepository;

    @Override
    @Transactional
    public ApiResponse insertBackupSettings(
            BackupSettingsJsonb backupSettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                BackupSettingsActionEnum
                        .INSERT_BACKUP_SETTINGS
                        .getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        BackupSettings backupSettings =
                new BackupSettings();

        backupSettings.setAutomaticBackup(
                booleanToByte(
                        backupSettingsJsonb.getAutomaticBackup()
                )
        );

        backupSettings.setBackupTime(
                backupSettingsJsonb.getBackupTime()
        );

        backupSettings.setRetentionPeriodDays(
                backupSettingsJsonb.getRetentionPeriodDays()
        );

        backupSettings.setBackupLocation(
                backupSettingsJsonb.getBackupLocation()
        );

        backupSettings.setCreatedAt(
                LocalDateTime.now()
        );

        backupSettings.setUpdatedAt(
                LocalDateTime.now()
        );

        backupSettingsRepository.persist(
                backupSettings
        );

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                BackupSettingsActionEnum
                        .INSERT_BACKUP_SETTINGS
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

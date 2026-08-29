package com.sbd.globalsetting.control.service;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.globalsetting.common.entity.SecuritySettings;
import com.sbd.globalsetting.common.enums.SecuritySettingsActionEnum;
import com.sbd.globalsetting.common.jsonb.SecuritySettingsJsonb;
import com.sbd.globalsetting.common.repository.SecuritySettingsRepository;
import com.sbd.globalsetting.control.SecuritySettingsControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@ApplicationScoped
public class SecuritySettingsService
        implements SecuritySettingsControl {

    @Inject
    SecuritySettingsRepository securitySettingsRepository;

    @Override
    @Transactional
    public ApiResponse insertSecuritySettings(
            SecuritySettingsJsonb securitySettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                SecuritySettingsActionEnum
                        .INSERT_SECURITY_SETTINGS
                        .getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        SecuritySettings securitySettings =
                new SecuritySettings();

        securitySettings.setSessionTimeoutMinutes(
                securitySettingsJsonb.getSessionTimeoutMinutes()
        );

        securitySettings.setPasswordExpiryDays(
                securitySettingsJsonb.getPasswordExpiryDays()
        );

        securitySettings.setMinimumPasswordLength(
                securitySettingsJsonb.getMinimumPasswordLength()
        );

        securitySettings.setRequireSpecialCharacters(
                booleanToByte(
                        securitySettingsJsonb
                                .getRequireSpecialCharacters()
                )
        );

        securitySettings.setEnableOtpLogin(
                booleanToByte(
                        securitySettingsJsonb
                                .getEnableOtpLogin()
                )
        );

        securitySettings.setAllowMultipleDeviceLogin(
                booleanToByte(
                        securitySettingsJsonb
                                .getAllowMultipleDeviceLogin()
                )
        );

        securitySettings.setCreatedAt(
                LocalDateTime.now()
        );

        securitySettings.setUpdatedAt(
                LocalDateTime.now()
        );

        securitySettingsRepository.persist(
                securitySettings
        );

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                SecuritySettingsActionEnum
                        .INSERT_SECURITY_SETTINGS
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

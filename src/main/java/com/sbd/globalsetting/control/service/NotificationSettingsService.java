package com.sbd.globalsetting.control.service;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.globalsetting.common.entity.NotificationSettings;
import com.sbd.globalsetting.common.enums.NotificationSettingsActionEnum;
import com.sbd.globalsetting.common.jsonb.NotificationSettingsJsonb;
import com.sbd.globalsetting.common.repository.NotificationSettingsRepository;
import com.sbd.globalsetting.control.NotificationSettingsControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@ApplicationScoped
public class NotificationSettingsService
        implements NotificationSettingsControl {

    @Inject
    NotificationSettingsRepository notificationSettingsRepository;

    @Override
    @Transactional
    public ApiResponse insertNotificationSettings(
            NotificationSettingsJsonb notificationSettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                NotificationSettingsActionEnum
                        .INSERT_NOTIFICATION_SETTINGS
                        .getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        NotificationSettings notificationSettings =
                new NotificationSettings();

        notificationSettings.setEmailNotifications(
                booleanToByte(
                        notificationSettingsJsonb
                                .getEmailNotifications()
                )
        );

        notificationSettings.setSmsNotifications(
                booleanToByte(
                        notificationSettingsJsonb
                                .getSmsNotifications()
                )
        );

        notificationSettings.setPushNotifications(
                booleanToByte(
                        notificationSettingsJsonb
                                .getPushNotifications()
                )
        );

        notificationSettings.setAttendanceAlerts(
                booleanToByte(
                        notificationSettingsJsonb
                                .getAttendanceAlerts()
                )
        );

        notificationSettings.setLeaveApprovalAlerts(
                booleanToByte(
                        notificationSettingsJsonb
                                .getLeaveApprovalAlerts()
                )
        );

        notificationSettings.setSalaryAlerts(
                booleanToByte(
                        notificationSettingsJsonb
                                .getSalaryAlerts()
                )
        );

        notificationSettings.setCreatedAt(
                LocalDateTime.now()
        );

        notificationSettings.setUpdatedAt(
                LocalDateTime.now()
        );

        notificationSettingsRepository.persist(
                notificationSettings
        );

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                NotificationSettingsActionEnum
                        .INSERT_NOTIFICATION_SETTINGS
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

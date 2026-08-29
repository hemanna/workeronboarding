package com.sbd.globalsetting.control;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.globalsetting.common.jsonb.NotificationSettingsJsonb;

public interface NotificationSettingsControl {

    ApiResponse insertNotificationSettings(
            NotificationSettingsJsonb notificationSettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException;

}

package com.sbd.globalsetting.boundary;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.globalsetting.common.jsonb.NotificationSettingsJsonb;
import com.sbd.globalsetting.control.NotificationSettingsControl;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Path("/settings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationSettingsResource {

    @Inject
    NotificationSettingsControl notificationSettingsControl;

    @POST
    @Path("/notification-settings")
    public ApiResponse insertNotificationSettings(
            NotificationSettingsJsonb notificationSettingsJsonb
    ) throws BusinessException, TechnicalException {
        String correlationId = UUID.randomUUID().toString();
        log.info("Request Id : {} | Insert Notification Settings", correlationId);

        return notificationSettingsControl
                .insertNotificationSettings(
                        notificationSettingsJsonb,
                        correlationId
                );
    }
}

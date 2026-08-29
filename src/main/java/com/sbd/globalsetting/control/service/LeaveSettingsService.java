package com.sbd.globalsetting.control.service;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.globalsetting.common.entity.LeaveSettings;
import com.sbd.globalsetting.common.enums.LeaveSettingsActionEnum;
import com.sbd.globalsetting.common.jsonb.LeaveSettingsJsonb;
import com.sbd.globalsetting.common.repository.LeaveSettingsRepository;
import com.sbd.globalsetting.control.LeaveSettingsControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@ApplicationScoped
public class LeaveSettingsService implements LeaveSettingsControl {

    @Inject
    LeaveSettingsRepository leaveSettingsRepository;

    @Override
    @Transactional
    public ApiResponse insertLeaveSettings(
            LeaveSettingsJsonb leaveSettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                LeaveSettingsActionEnum
                        .INSERT_LEAVE_SETTINGS
                        .getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        LeaveSettings leaveSettings = new LeaveSettings();

        leaveSettings.setAnnualLeaveDays(
                leaveSettingsJsonb.getAnnualLeaveDays()
        );

        leaveSettings.setSickLeaveDays(
                leaveSettingsJsonb.getSickLeaveDays()
        );

        leaveSettings.setCasualLeaveDays(
                leaveSettingsJsonb.getCasualLeaveDays()
        );

        leaveSettings.setMaximumCarryForward(
                leaveSettingsJsonb.getMaximumCarryForward()
        );

        leaveSettings.setApprovalLevel(
                leaveSettingsJsonb.getApprovalLevel()
        );

        leaveSettings.setLossOfPayAllowed(
                booleanToByte(
                        leaveSettingsJsonb.getLossOfPayAllowed()
                )
        );

        leaveSettings.setCarryForwardLeave(
                booleanToByte(
                        leaveSettingsJsonb.getCarryForwardLeave()
                )
        );

        leaveSettings.setAllowLeaveCancellation(
                booleanToByte(
                        leaveSettingsJsonb.getAllowLeaveCancellation()
                )
        );

        leaveSettings.setCreatedAt(
                LocalDateTime.now()
        );

        leaveSettings.setUpdatedAt(
                LocalDateTime.now()
        );

        leaveSettingsRepository.persist(leaveSettings);

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                LeaveSettingsActionEnum
                        .INSERT_LEAVE_SETTINGS
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

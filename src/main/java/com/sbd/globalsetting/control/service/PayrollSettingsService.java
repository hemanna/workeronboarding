package com.sbd.globalsetting.control.service;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.globalsetting.common.entity.PayrollSettings;
import com.sbd.globalsetting.common.enums.PayrollSettingsActionEnum;
import com.sbd.globalsetting.common.jsonb.PayrollSettingsJsonb;
import com.sbd.globalsetting.common.repository.PayrollSettingsRepository;
import com.sbd.globalsetting.control.PayrollSettingsControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@ApplicationScoped
public class PayrollSettingsService implements PayrollSettingsControl {

    @Inject
    PayrollSettingsRepository payrollSettingsRepository;

    @Override
    @Transactional
    public ApiResponse insertPayrollSettings(
            PayrollSettingsJsonb payrollSettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                PayrollSettingsActionEnum
                        .INSERT_PAYROLL_SETTINGS
                        .getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        PayrollSettings payrollSettings =
                new PayrollSettings();

        payrollSettings.setSalaryCycle(
                payrollSettingsJsonb.getSalaryCycle()
        );

        payrollSettings.setSalaryCalculation(
                payrollSettingsJsonb.getSalaryCalculation()
        );

        payrollSettings.setPayslipPassword(
                payrollSettingsJsonb.getPayslipPassword()
        );

        payrollSettings.setEnablePf(
                booleanToByte(
                        payrollSettingsJsonb.getEnablePf()
                )
        );

        payrollSettings.setEnableEsi(
                booleanToByte(
                        payrollSettingsJsonb.getEnableEsi()
                )
        );

        payrollSettings.setEnableProfessionalTax(
                booleanToByte(
                        payrollSettingsJsonb
                                .getEnableProfessionalTax()
                )
        );

        payrollSettings.setAutoGeneratePayslip(
                booleanToByte(
                        payrollSettingsJsonb
                                .getAutoGeneratePayslip()
                )
        );

        payrollSettings.setCreatedAt(
                LocalDateTime.now()
        );

        payrollSettings.setUpdatedAt(
                LocalDateTime.now()
        );

        payrollSettingsRepository.persist(
                payrollSettings
        );

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                PayrollSettingsActionEnum
                        .INSERT_PAYROLL_SETTINGS
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

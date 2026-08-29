package com.sbd.globalsetting.control.service;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.globalsetting.common.entity.CompanySettings;
import com.sbd.globalsetting.common.enums.CompanySettingsActionEnum;
import com.sbd.globalsetting.common.jsonb.CompanySettingsJsonb;
import com.sbd.globalsetting.common.repository.CompanySettingsRepository;
import com.sbd.globalsetting.control.CompanySettingsControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class CompanySettingsService
        implements CompanySettingsControl {

    @Inject
    CompanySettingsRepository companySettingsRepository;

    @Override
    @Transactional
    public ApiResponse insertCompanySettings(
            CompanySettingsJsonb companySettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                CompanySettingsActionEnum
                        .INSERT_COMPANY_SETTINGS
                        .getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        CompanySettings companySettings =
                new CompanySettings();

        /*
         * Multipart image -> byte[] -> Database BLOB
         */
        if (companySettingsJsonb.getCompanyLogo() != null) {

            companySettings.setCompanyLogo(
                    companySettingsJsonb.getCompanyLogo()
            );
        }

        companySettings.setCompanyName(
                companySettingsJsonb.getCompanyName()
        );

        companySettings.setEmail(
                companySettingsJsonb.getEmail()
        );

        companySettings.setPhoneNumber(
                companySettingsJsonb.getPhoneNumber()
        );

        companySettings.setWebsite(
                companySettingsJsonb.getWebsite()
        );

        companySettings.setGstNumber(
                companySettingsJsonb.getGstNumber()
        );

        companySettings.setPanNumber(
                companySettingsJsonb.getPanNumber()
        );

        companySettings.setCompanyAddress(
                companySettingsJsonb.getCompanyAddress()
        );

        companySettings.setCreatedAt(
                LocalDateTime.now()
        );

        companySettings.setUpdatedAt(
                LocalDateTime.now()
        );

        companySettingsRepository.persist(
                companySettings
        );

        companySettingsRepository.flush();

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                CompanySettingsActionEnum
                        .INSERT_COMPANY_SETTINGS
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
}

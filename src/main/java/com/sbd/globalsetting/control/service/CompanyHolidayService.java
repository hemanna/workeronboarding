package com.sbd.globalsetting.control.service;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.globalsetting.common.entity.CompanyHolidays;
import com.sbd.globalsetting.common.enums.CompanyHolidayActionEnum;
import com.sbd.globalsetting.common.jsonb.CompanyHolidayJsonb;
import com.sbd.globalsetting.common.repository.CompanyHolidayRepository;
import com.sbd.globalsetting.control.CompanyHolidayControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
public class CompanyHolidayService
        implements CompanyHolidayControl {

    @Inject
    CompanyHolidayRepository companyHolidayRepository;

    @Override
    @Transactional
    public ApiResponse insertHoliday(
            CompanyHolidayJsonb companyHolidayJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                CompanyHolidayActionEnum
                        .INSERT_HOLIDAY
                        .getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        CompanyHolidays companyHoliday =
                new CompanyHolidays();

        companyHoliday.setHolidayCalendar(
                companyHolidayJsonb.getHolidayCalendar()
        );

        companyHoliday.setHolidayName(
                companyHolidayJsonb.getHolidayName()
        );

        companyHoliday.setHolidayDate(
                companyHolidayJsonb.getHolidayDate()
        );

        companyHoliday.setHolidayType(
                companyHolidayJsonb.getHolidayType()
        );

        companyHoliday.setReason(
                companyHolidayJsonb.getReason()
        );

        companyHolidayRepository.persist(
                companyHoliday
        );

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                CompanyHolidayActionEnum
                        .INSERT_HOLIDAY
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

    @Override
    public ApiResponse getHolidays(
            String holidayCalendar,
            String correlationId
    ) throws BusinessException, TechnicalException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                CompanyHolidayActionEnum
                        .GET_HOLIDAYS
                        .getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        List<CompanyHolidays> holidays;

        if (holidayCalendar == null
                || holidayCalendar.isBlank()) {

            holidays =
                    companyHolidayRepository
                            .findAllHolidays();

        } else {

            holidays =
                    companyHolidayRepository
                            .findByCalendar(
                                    holidayCalendar
                            );
        }

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                CompanyHolidayActionEnum
                        .GET_HOLIDAYS
                        .getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                ),
                holidays
        );
    }
}
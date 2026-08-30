package com.sbd.globalsetting.control;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.globalsetting.common.jsonb.CompanyHolidayJsonb;

public interface CompanyHolidayControl {
    ApiResponse insertHoliday(
            CompanyHolidayJsonb companyHolidayJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException;

    ApiResponse getHolidays(
            String holidayCalendar,
            String correlationId
    ) throws BusinessException, TechnicalException;
}


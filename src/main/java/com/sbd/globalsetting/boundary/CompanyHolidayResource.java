package com.sbd.globalsetting.boundary;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.globalsetting.common.jsonb.CompanyHolidayJsonb;
import com.sbd.globalsetting.control.CompanyHolidayControl;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("/settings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompanyHolidayResource {

    @Inject
    CompanyHolidayControl companyHolidayControl;

    @POST
    @Path("/holidays")
    public ApiResponse insertHoliday(
            CompanyHolidayJsonb companyHolidayJsonb
    ) throws BusinessException, TechnicalException {
        String correlationId = UUID.randomUUID().toString();

        return companyHolidayControl.insertHoliday(
                companyHolidayJsonb,
                correlationId
        );
    }

    @GET
    @Path("/holidays")
    public ApiResponse getHolidays(
            @QueryParam("holidayCalendar")
            String holidayCalendar
    ) throws BusinessException, TechnicalException {
        String correlationId = UUID.randomUUID().toString();

        return companyHolidayControl.getHolidays(
                holidayCalendar,
                correlationId
        );
    }
}

package com.sbd.globalsetting.boundary;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.globalsetting.common.jsonb.PayrollSettingsJsonb;
import com.sbd.globalsetting.control.PayrollSettingsControl;
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
public class PayrollSettingsResource {

    @Inject
    PayrollSettingsControl payrollSettingsControl;

    @POST
    @Path("/payroll-settings")
    public ApiResponse insertPayrollSettings(
            PayrollSettingsJsonb payrollSettingsJsonb
    ) throws BusinessException, TechnicalException {
        String correlationId = UUID.randomUUID().toString();
        log.info("Request Id : {} | Insert Payroll Settings", correlationId);

        return payrollSettingsControl.insertPayrollSettings(
                payrollSettingsJsonb,
                correlationId
        );
    }
}
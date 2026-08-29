package com.sbd.globalsetting.boundary;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.globalsetting.common.jsonb.CompanySettingsJsonb;
import com.sbd.globalsetting.control.CompanySettingsControl;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Path("/settings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompanySettingsResource {

    @Inject
    CompanySettingsControl companySettingsControl;

    @POST
    @Path("/company-settings")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public ApiResponse insertCompanySettings(
            @BeanParam CompanySettingsJsonb companySettingsJsonb
    ) throws BusinessException, TechnicalException {
        String correlationId = UUID.randomUUID().toString();

        return companySettingsControl.insertCompanySettings(
                companySettingsJsonb,
                correlationId
        );
    }
}

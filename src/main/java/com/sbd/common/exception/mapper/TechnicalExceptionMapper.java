package com.sbd.common.exception.mapper;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class TechnicalExceptionMapper implements ExceptionMapper<TechnicalException> {
    @Override
    public Response toResponse(TechnicalException te) {
        log.error(
                LogEnum.END_METHOD_FAILURE.getValue(),
                te.getRequestId(),
                te.getStatus(),
                te.getMessage(),
                te.getCause());
        ApiResponse apiResponse =
                new ApiResponse(new Status(te.getStatus(), te.getMessage(), te.getRequestId()));
        return Response.status(te.getStatus()).entity(apiResponse).build();
    }
}

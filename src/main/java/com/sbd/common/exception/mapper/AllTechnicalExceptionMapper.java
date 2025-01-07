package com.sbd.common.exception.mapper;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Provider
@Slf4j
public class AllTechnicalExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        String requestId = UUID.randomUUID().toString();
        log.error(LogEnum.EXCEPTION_FAILURE.getValue(), requestId, e.getMessage(), e);
        ApiResponse apiResponse =
                new ApiResponse(
                        new Status(
                                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                StatusCodeEnum.TECHNICAL_FAILURE.getValue(),
                                requestId,
                                e.getMessage()));
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .entity(apiResponse)
                .build();
    }
}

package com.sbd.common.exception.mapper;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Provider
@Slf4j
public class NotAllowedExceptionMapper implements ExceptionMapper<NotAllowedException> {

    @Override
    public Response toResponse(NotAllowedException e) {
        String requestId = UUID.randomUUID().toString();
        log.warn(LogEnum.EXCEPTION_FAILURE.getValue(), requestId, e.getMessage(), e);

        ApiResponse apiResponse =
                new ApiResponse(
                        new Status(
                                Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), e.getMessage(), requestId));
        return Response.status(Response.Status.METHOD_NOT_ALLOWED.getStatusCode())
                .entity(apiResponse)
                .build();
    }
}

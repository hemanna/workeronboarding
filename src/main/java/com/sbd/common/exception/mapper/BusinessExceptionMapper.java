package com.sbd.common.exception.mapper;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException be) {
        log.info(
                LogEnum.END_METHOD_RESPONSE.getValue(),
                be.getRequestId(),
                be.getStatus(),
                be.getMessage());
        ApiResponse apiResponse =
                new ApiResponse(new Status(be.getStatus(), be.getMessage(), be.getRequestId()));
        return Response.status(be.getStatus()).entity(apiResponse).build();
    }
}

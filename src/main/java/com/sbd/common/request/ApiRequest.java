package com.sbd.common.request;

import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import jakarta.ws.rs.core.Response;
import lombok.Data;

import java.util.Objects;
import java.util.stream.Stream;

@Data
public class ApiRequest<T> {
    private T data;
    private String type;
    private Pagination pagination;

    public void isValid(String requestId) throws BusinessException {
        if (!Stream.of(data, type).allMatch(Objects::nonNull)) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    requestId,
                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue());
        }
    }

    public void isValidData(String requestId) throws BusinessException {
        if (!Stream.of(data).allMatch(Objects::nonNull)) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    requestId,
                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue());
        }
    }

    public void isValidPagination(String requestId) throws BusinessException {
        if (!Stream.of(pagination).allMatch(Objects::nonNull)) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    requestId,
                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue());
        }
    }
}

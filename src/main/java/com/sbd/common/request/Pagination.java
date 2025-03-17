package com.sbd.common.request;

import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import jakarta.ws.rs.core.Response;
import lombok.Data;

@Data
public class Pagination {
    private Integer pageIndex = 0;
    private Integer pageSize = 0;

    public void isValid(String requestId) throws BusinessException {
        if (pageSize == 0) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    requestId,
                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue());
        }

    }
}

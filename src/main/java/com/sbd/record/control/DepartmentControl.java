package com.sbd.record.control;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.response.ApiResponse;

public interface DepartmentControl {

    ApiResponse fetchAllDepartments
            (String correlationId)
            throws BusinessException;

}

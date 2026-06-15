package com.sbd.record.control;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.response.ApiResponse;

public interface LeaveTypeControl {

    ApiResponse fetchAllLeaveTypes
            (String correlationId)
            throws BusinessException;
}

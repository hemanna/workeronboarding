package com.sbd.record.control;

import com.sbd.common.Jsonb.BankDetailsJsonb;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.response.ApiResponse;

public interface BankDetailsControl {

    ApiResponse bankHolderLogin(
            ApiRequest<BankDetailsJsonb> apiRequest,
            String requestId);

    ApiResponse updateBankDetailsRequest(
            Integer employeeId,
            ApiRequest<BankDetailsJsonb> apiRequest,
            String requestId)
            throws BusinessException;

    ApiResponse fetchBankDetailsEmployeeById(
            Long employeeId,
            String requestId )
            throws BusinessException;

}

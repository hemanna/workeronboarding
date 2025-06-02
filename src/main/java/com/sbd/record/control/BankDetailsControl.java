package com.sbd.record.control;

import com.sbd.common.Jsonb.BankDetailsJsonb;
import com.sbd.common.Jsonb.UserCredentialRequest;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDetailsRequest;
import com.sbd.common.response.ApiResponse;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

public interface BankDetailsControl {
    ApiResponse bankHolderLogin(ApiRequest<BankDetailsJsonb> apiRequest, String requestId);

}

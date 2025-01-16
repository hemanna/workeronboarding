package com.sbd.record.control;

import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.UserCredentialsDTO;
import com.sbd.common.response.ApiResponse;

public interface UserCredentialControl {
    ApiResponse resetPassword(ApiRequest<UserCredentialsDTO> apiRequest, String requestId);  // PATCH for resetting password
    ApiResponse createUserCredentials(ApiRequest<UserCredentialsDTO> apiRequest,String requestId);
}

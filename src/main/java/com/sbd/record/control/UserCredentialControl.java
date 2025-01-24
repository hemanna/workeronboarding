package com.sbd.record.control;

import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.UserCredentialsDTO;
import com.sbd.common.response.ApiResponse;

public interface UserCredentialControl {
    ApiResponse createUserCredentials(ApiRequest<UserCredentialsDTO> apiRequest, String requestId);

    ApiResponse resetPassword(String username, String password, String requestId);
}

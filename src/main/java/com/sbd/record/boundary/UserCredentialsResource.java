package com.sbd.record.boundary;

import com.sbd.common.Jsonb.ResetPasswordRequest;
import com.sbd.common.Jsonb.UserCredentialRequest;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.UserCredentialControl;
import com.sbd.record.control.service.UserCredentialService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/employee")
@AllArgsConstructor
@Slf4j
public class UserCredentialsResource {

    private final UserCredentialService userCredentialService;
    private final UserCredentialControl userCredentialControl;

    @POST
    @Path("/user-Login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse UserLogin(ApiRequest<UserCredentialRequest> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Login User Request: {}", requestId, apiRequest);
        return userCredentialControl.UserLogin(apiRequest, requestId);
    }

    @PATCH
    @Path("/reset-password")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse resetPassword(ApiRequest<ResetPasswordRequest> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Reset Password Request:{}", requestId,apiRequest);
        return userCredentialControl.resetPassword(apiRequest, requestId);
    }
}

package com.sbd.record.boundary;

import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.UserCredentialsDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.UserCredentialControl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/employee")
@AllArgsConstructor
@Slf4j
public class UserCredentialsResource {

    private final UserCredentialControl userCredentialControl;

    @POST
    @Path("/user-credentials")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse createUserCredentials(ApiRequest<UserCredentialsDTO> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Create User Credentials Request: {}", requestId, apiRequest);
        return userCredentialControl.createUserCredentials(apiRequest, requestId);
    }

    @PATCH
    @Path("/reset-password")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse resetPassword(ApiRequest<UserCredentialsDTO> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        String username = apiRequest.getData().getUsername();
        String password = apiRequest.getData().getPassword();

        log.info("RequestId: {} | Reset Password Request: username={}, password=[PROTECTED]", requestId, username);

        return userCredentialControl.resetPassword(username, password, requestId);
    }


}

package com.sbd.record.control.service;

import com.sbd.common.Jsonb.UserCredentialRequest;
import com.sbd.common.entity.EmployeeDetails;
import com.sbd.common.entity.UserCredentials;
import com.sbd.common.mapper.EmployeeDetailsMapper;
import com.sbd.common.repository.EmployeeDetailsRepository;
import com.sbd.common.repository.RoleRepository;
import com.sbd.common.repository.UserCredentialsRepository;
import com.sbd.common.request.*;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.UserCredentialControl;
import io.netty.handler.codec.haproxy.HAProxySSLTLV;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@ApplicationScoped
@Slf4j
public class UserCredentialService implements UserCredentialControl {

    @Inject
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private UserCredentialsRepository userCredentialsRepository;

    @Override
    @Transactional
    public ApiResponse UserLogin(ApiRequest<UserCredentialRequest> apiRequest, String requestId) {
        log.info("Start Login user  - RequestId: {}", requestId);



        // Validate request data
        UserCredentialRequest userRequest = apiRequest.getData();
        if (userRequest == null || userRequest.getUsername() == null || userRequest.getPassword() == null) {
            log.warn("RequestId: {} | Invalid login request: missing fields", requestId);
            return new ApiResponse(new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Username and Password are required", requestId));
        }

        // Check if user exists
        EmployeeDetails user = employeeDetailsRepository.findByEmailOrPhone(userRequest.getUsername());
        if (user == null) {
            log.warn("RequestId: {} | User not found: {}", requestId, userRequest.getUsername());
            return new ApiResponse(new Status(Response.Status.NOT_FOUND.getStatusCode(), "User not found", requestId));
        }

        // Verify password
        if (!user.getPassword().equals(userRequest.getPassword())) {
            log.warn("RequestId: {} | Incorrect password for user: {}", requestId, userRequest.getUsername());
            return new ApiResponse(new Status(Response.Status.UNAUTHORIZED.getStatusCode(), "Incorrect Password", requestId));
        }

// Convert EmployeeDetails to EmployeeDetailsDTO using Mapper
        EmployeeDTO.EmployeeDetailsDTO employeeDetailsDTO = EmployeeDetailsMapper.INSTANCE.toDTO(user);


        log.info("End user Login  - RequestId: {}", requestId);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Successful", requestId),employeeDetailsDTO
        );
    }



    @Transactional
    public ApiResponse resetPassword(ApiRequest<ResetPasswordRequest> apiRequest, String requestId) {
        log.info("Start resetting password - RequestId: {}", requestId);
        ResetPasswordRequest requestData = apiRequest.getData();
        String username = requestData.getUserName();

        // Validate request
        if (requestData == null || requestData.getNewPassword() == null || requestData.getConfirmNewPassword() == null) {
            return new ApiResponse(new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid request payload", requestId));
        }

        if (!requestData.getNewPassword().equals(requestData.getConfirmNewPassword())) {
            return new ApiResponse(new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Passwords do not match", requestId));
        }

        // Perform user verification (pseudo-code, implement actual logic)
        boolean isValidUser = validateUser(username, requestData.getCurrentPassword());
        if (!isValidUser) {
            return new ApiResponse(new Status(Response.Status.UNAUTHORIZED.getStatusCode(), "Invalid credentials", requestId));
        }

        // Update password in database (pseudo-code, implement actual logic)
        boolean passwordUpdated = updatePassword(username, requestData.getNewPassword());
        if (!passwordUpdated) {
            return new ApiResponse(new Status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Failed to reset password", requestId));
        }

        log.info("End resetting password - RequestId: {}", requestId);
        return new ApiResponse(new Status(Response.Status.OK.getStatusCode(), "Password reset successfully", requestId));
    }

    private boolean validateUser(String username, String currentPassword) {
        // Implement actual user validation logic (e.g., query database, compare passwords)
        return true; // Placeholder
    }

    private boolean updatePassword(String username, String newPassword) {
        // Implement actual password update logic in the database
        return true; // Placeholder
    }
}



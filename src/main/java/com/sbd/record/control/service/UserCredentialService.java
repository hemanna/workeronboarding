package com.sbd.record.control.service;

import com.sbd.common.Jsonb.ResetPasswordRequest;
import com.sbd.common.Jsonb.UserCredentialRequest;
import com.sbd.common.entity.EmployeeDetails;
import com.sbd.common.mapper.EmployeeDetailsMapper;
import com.sbd.common.repository.EmployeeDetailsRepository;
import com.sbd.common.repository.RoleRepository;
import com.sbd.common.request.*;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.UserCredentialControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;


@ApplicationScoped
@Slf4j
public class UserCredentialService implements UserCredentialControl {

    @Inject
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Inject
    private RoleRepository roleRepository;

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

        // Check if employeedetails exists
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

      // Convert EmployeeDetails to DTO using Mapper
        EmployeeDTO.EmployeeDetailsDTO employeeDetailsDTO = EmployeeDetailsMapper.INSTANCE.toDTO(user);


        log.info("End user Login  - RequestId: {}", requestId);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Successful", requestId),employeeDetailsDTO
        );
    }

    @Override
    @Transactional
    public ApiResponse resetPassword(ApiRequest<ResetPasswordRequest> apiRequest, String requestId) {
        log.info("Start resetting password - RequestId: {}", requestId);
        ResetPasswordRequest requestData = apiRequest.getData();
        if (requestData == null || requestData.getNewPassword() == null || requestData.getConfirmNewPassword() == null) {
            return new ApiResponse(new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid request payload", requestId));
        }

        if (!requestData.getNewPassword().equals(requestData.getConfirmNewPassword())) {
            return new ApiResponse(new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Passwords do not match", requestId));
        }

        // Retrieve Employee details from DB
        EmployeeDetails user = employeeDetailsRepository.findByEmailOrPhone(requestData.getUserName());
        if (user == null) {
            return new ApiResponse(new Status(Response.Status.NOT_FOUND.getStatusCode(), "User not found", requestId));
        }

        // Check if current password matches
        if (!user.getPassword().equals(requestData.getCurrentPassword())) {
            return new ApiResponse(new Status(Response.Status.UNAUTHORIZED.getStatusCode(), "Invalid current password", requestId));
        }

        // Update password
        user.setPassword(requestData.getNewPassword());
        employeeDetailsRepository.persist(user);

        log.info("End resetting password - RequestId: {}", requestId);
        return new ApiResponse(new Status(Response.Status.OK.getStatusCode(), "Password reset successfully", requestId));
    }
}



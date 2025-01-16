package com.sbd.record.control.service;

import com.sbd.common.entity.EmployeeDetails;
import com.sbd.common.entity.UserCredentials;
import com.sbd.common.repository.EmployeeDetailsRepository;
import com.sbd.common.repository.UserCredentialsRepository;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.UserCredentialsDTO;
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
    private UserCredentialsRepository userCredentialsRepository;

    @Inject
    private EmployeeDetailsRepository employeeDetailsRepository;


    @Override
    @Transactional
    public ApiResponse createUserCredentials(ApiRequest<UserCredentialsDTO> apiRequest, String requestId) {
        log.info("Start creating user credentials - RequestId: {}", requestId);

        UserCredentialsDTO userCredentialsDTO = apiRequest.getData();

        // Check if user already exists
        UserCredentials existingUser = userCredentialsRepository.findByUsername(userCredentialsDTO.getUsername());
        if (existingUser != null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Username already exists", requestId)
            );
        }

        // Fetch employee details using the employeeId from DTO
        EmployeeDetails employee = employeeDetailsRepository.findById(userCredentialsDTO.getEmployeeId());
        if (employee == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Create UserCredentials
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(userCredentialsDTO.getUsername());
        userCredentials.setPassword(userCredentialsDTO.getPassword());
        userCredentials.setEmployee(employee);
        userCredentialsRepository.persist(userCredentials);

        log.info("End creating user credentials - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "true", requestId));
    }


    @Override
    @Transactional
    public ApiResponse resetPassword(ApiRequest<UserCredentialsDTO> apiRequest, String requestId) {
        log.info("Start resetting password - RequestId: {}", requestId);

        UserCredentialsDTO userCredentialsDTO = apiRequest.getData();

        // Fetch the user credentials by employee ID
        UserCredentials userCredentials = userCredentialsRepository.findByEmployeeId(userCredentialsDTO.getEmployeeId());
        if (userCredentials == null) {
            log.error("UserCredentials not found - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "User credentials not found", requestId)
            );
        }

        // Reset the password
        userCredentials.setPassword(userCredentialsDTO.getPassword());
        userCredentialsRepository.persist(userCredentials);

        log.info("End resetting password - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Password reset successfully", requestId)

        );
    }

}

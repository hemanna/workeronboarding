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



    @Override
    @Transactional
    public ApiResponse resetPassword(String username, String password, String requestId) {
        log.info("Start resetting password - RequestId: {}", requestId);

        // Fetch the user credentials
        UserCredentials userCredentials = userCredentialsRepository.findByUsername(username);
        if (userCredentials == null) {
            log.error("UserCredentials not found - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Invalid UserName", requestId)
            );
        }

        // Reset the password
        userCredentials.setPassword(password);
        userCredentialsRepository.persist(userCredentials);

        log.info("End resetting password - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Password reset successfully", requestId)
        );
    }
}



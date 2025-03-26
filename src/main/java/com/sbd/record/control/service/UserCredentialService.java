package com.sbd.record.control.service;

import com.sbd.common.entity.UserCredentials;
import com.sbd.common.repository.EmployeeDetailsRepository;
import com.sbd.common.repository.RoleRepository;
import com.sbd.common.repository.UserCredentialsRepository;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.UserCredentialsDTO;
import com.sbd.common.request.LoginRequest;
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
    public ApiResponse createUserCredentials(ApiRequest<UserCredentialsDTO> apiRequest, String requestId) {
        log.info("Start creating user credentials - RequestId: {}", requestId);

//        Optional<UserCredentials> userOptional = userCredentialsRepository.findByUsernameOrPhoneNumber(LoginRequest.getUsername());
//
//        if (userOptional.isEmpty()) {
//            return new ApiResponse(new Status(401, "Invalid credentials", null));
//        }
//
//        UserCredentials user = userOptional.get();
//
//        // Validate Password
//        HAProxySSLTLV passwordHash;
//        if (!passwordHash.verify(user.getPassword(), LoginRequest.getPassword().toCharArray())) {
//            return new ApiResponse(new Status(401, "Invalid credentials", null));
//        }



        log.info("End creating user credentials - RequestId: {}", requestId);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Successful", requestId)
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



package com.sbd.record.control.service;

import com.sbd.common.Jsonb.BankDetailsJsonb;
import com.sbd.common.entity.BankDetails;
import com.sbd.common.entity.EmployeeDetails;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.repository.BankDetailsRepository;
import com.sbd.common.repository.EmployeeDetailsRepository;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.BankDetailsControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@ApplicationScoped
@Slf4j
public class BankDetailsService implements BankDetailsControl {

    @Inject
    private BankDetailsRepository bankDetailsRepository;

    @Inject
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Override
    @Transactional
    public ApiResponse bankHolderLogin(ApiRequest<BankDetailsJsonb> apiRequest, String requestId) {
        log.info("Start creating BankDetails - RequestId: {}", requestId);

        BankDetailsJsonb details = apiRequest.getData();

        // Fetch the employee details
        EmployeeDetails employee = employeeDetailsRepository.findById(details.getEmployeeId());
        if (employee == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        BankDetails existing = bankDetailsRepository.findByEmployeeId(details.getEmployeeId());
        if (existing != null) {
            return new ApiResponse(
                    new Status(Response.Status.CONFLICT.getStatusCode(), "Bank details already exist for this employee", requestId)
            );
        }


        BankDetails bankDetails = new BankDetails();
        bankDetails.setEmployeeId(employee);
        bankDetails.setAccountNumber(details.getAccountNumber());
        bankDetails.setIfscCode(details.getIfscCode());
        bankDetails.setBankName(details.getBankName());
        bankDetails.setBranchName(details.getBranchName());
        bankDetails.setNameOnAccount(details.getNameOnAccount());
        bankDetails.setAccountCountryTerritory(details.getAccountCountryTerritory());
        bankDetails.setAccountType(details.getAccountType());
        bankDetails.setCreatedAt(LocalDateTime.now());
        bankDetails.setUpdatedAt(LocalDateTime.now());

        bankDetailsRepository.persist(bankDetails);
        employeeDetailsRepository.flush();

        log.info("End creating Bank Details - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Bank holder details successfully created", requestId)

        );    }

    @Override
    @Transactional
    public ApiResponse updateBankDetailsRequest(
            Integer employeeId,
            ApiRequest<BankDetailsJsonb> apiRequest,
            String requestId)
            throws BusinessException {
        log.info("Start updating BankDetails - RequestId: {}", requestId);

        BankDetailsJsonb details = apiRequest.getData();

        // Fetch the employee details
        EmployeeDetails employee = employeeDetailsRepository.findById(employeeId);
        if (employee == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Fetch existing bank details for the employee
        BankDetails bankDetails = bankDetailsRepository.findByEmployeeId(employeeId);
        if (bankDetails == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Bank details not found for this employee", requestId)
            );
        }

        // Update bank details
        bankDetails.setAccountNumber(details.getAccountNumber());
        bankDetails.setIfscCode(details.getIfscCode());
        bankDetails.setBankName(details.getBankName());
        bankDetails.setBranchName(details.getBranchName());
        bankDetails.setNameOnAccount(details.getNameOnAccount());
        bankDetails.setAccountCountryTerritory(details.getAccountCountryTerritory());
        bankDetails.setAccountType(details.getAccountType());
        bankDetails.setUpdatedAt(LocalDateTime.now());

        bankDetailsRepository.persist(bankDetails);
        employeeDetailsRepository.flush();

        log.info("End updating BankDetails - RequestId: {}", requestId);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Bank details successfully updated", requestId)
        );
    }
}

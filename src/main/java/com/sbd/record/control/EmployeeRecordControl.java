package com.sbd.record.control;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDetailsRequest;
import com.sbd.common.response.ApiResponse;

public interface EmployeeRecordControl {
    ApiResponse createEmployeeDetails(
            EmployeeDetailsRequest employeeDetailsRequest,
            String requestId)
            throws BusinessException;

    ApiResponse updateEmployeeDetails(
            Integer employeeId,
            EmployeeDetailsRequest employeeDetailsRequest,
            String requestId)
            throws BusinessException;

    ApiResponse updateApprovalStatus(
            Integer employeeId,
            String approvalStatus,
            String requestId);

    ApiResponse fetchEmployeeById(
            Long employeeId,
            String requestId );

ApiResponse fetchAllEmployee(
        String requestId ,
        ApiRequest<EmployeeDetailsRequest> apiRequest)
        throws BusinessException;

    ApiResponse deleteEmployee(
            String requestId,
            Long employeeId)
            throws BusinessException;

    ApiResponse skillcountEmployee(
            String requestId ,
            ApiRequest<EmployeeDetailsRequest> apiRequest)
            throws BusinessException;

    ApiResponse fetchEmployeeDashboard(
            String correlationId) throws BusinessException;
}

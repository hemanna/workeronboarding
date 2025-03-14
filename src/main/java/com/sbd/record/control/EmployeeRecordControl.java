package com.sbd.record.control;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDetailsRequest;
import com.sbd.common.response.ApiResponse;

public interface EmployeeRecordControl {
    ApiResponse createEmployeeDetails(EmployeeDetailsRequest employeeDetailsRequest, String requestId) throws BusinessException;

//    ApiResponse updateEmployeeDetails(Integer employeeId, ApiRequest<EmployeeDTO> apiRequest, String requestId);
    ApiResponse updateApprovalStatus(Integer employeeId, String approvalStatus, String requestId);
    ApiResponse fetchEmployeeById(Long employeeId, String requestId );
    ApiResponse fetchAllEmployees(String requestId);
//    ApiResponse fetchPendingApprovals(String requestId);
    }

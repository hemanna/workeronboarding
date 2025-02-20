package com.sbd.record.control;

import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.UserCredentialsDTO;
import com.sbd.common.response.ApiResponse;
import io.netty.handler.codec.http.multipart.FileUpload;

public interface EmployeeRecordControl {
    ApiResponse createEmployeeDetails(ApiRequest<EmployeeDTO> apiRequest, String requestId);
    ApiResponse updateEmployeeDetails(Integer employeeId, ApiRequest<EmployeeDTO> apiRequest, String requestId);
    ApiResponse updateApprovalStatus(Integer employeeId, String approvalStatus, String requestId);
    ApiResponse fetchEmployeeById(Long employeeId, String requestId );
    ApiResponse fetchAllEmployees(String requestId);
    ApiResponse fetchPendingApprovals(String requestId);
//    ApiResponse uploadEmployeeImages(Integer employeeId, FileUpload profilePic, FileUpload aadharPic, FileUpload pancardPic, String requestId);
}

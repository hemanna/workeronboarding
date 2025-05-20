package com.sbd.record.control;

import com.sbd.common.Jsonb.LeaveBalanceJsonb;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.LeaveRequest;
import com.sbd.common.response.ApiResponse;
import io.vertx.ext.web.FileUpload;

import java.io.IOException;
import java.util.List;

public interface LeaveControl {
    ApiResponse createLeaveRequest(LeaveRequest leaveRequest , String requestId)throws IOException, BusinessException;
    ApiResponse updateLeaveRequest(Integer leaveId, ApiRequest<EmployeeDTO> apiRequest, String requestId);
    ApiResponse<EmployeeDTO.LeaveDTO> fetchLeaveById(Long leaveId, String requestId);
    ApiResponse fetchAllLeaveRequest(String requestId);
    ApiResponse updateApprovalStatus(Integer leaveId, String approvalStatus, String requestId);
    ApiResponse <List<LeaveBalanceJsonb>> fetchLeaveBalancesByEmployeeId(int employeeId, int year, String requestId);
        ;

}



package com.sbd.record.control;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.LeaveRequest;
import com.sbd.common.response.ApiResponse;
import io.vertx.ext.web.FileUpload;

import java.io.IOException;

public interface LeaveControl {
    ApiResponse createLeaveRequest(LeaveRequest leaveRequest , String requestId)throws IOException, BusinessException;
    ApiResponse updateLeaveRequest(Integer leaveId, ApiRequest<EmployeeDTO> apiRequest, String requestId);
    ApiResponse<EmployeeDTO.LeaveDTO> fetchLeaveById(Long leaveId, String requestId);
    ApiResponse fetchAllLeaveRequest(String requestId);

}



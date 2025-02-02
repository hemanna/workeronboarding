package com.sbd.record.control;

import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;

public interface LeaveControl {
    ApiResponse createLeaveRequest(ApiRequest<EmployeeDTO> apiRequest, String requestId);
    ApiResponse updateLeaveRequest(Integer leaveId, ApiRequest<EmployeeDTO> apiRequest, String requestId);
    ApiResponse<EmployeeDTO.LeaveDTO> fetchLeaveById(Long leaveId, String requestId);
    ApiResponse fetchAllLeaveRequest(String requestId);

}

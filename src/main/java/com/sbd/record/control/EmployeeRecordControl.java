package com.sbd.record.control;

import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.UserDTO;
import com.sbd.common.response.ApiResponse;

public interface EmployeeRecordControl {
    ApiResponse createEmployeeDetails(ApiRequest<EmployeeDTO> apiRequest, String requestId);
    ApiResponse createAttendance(ApiRequest<EmployeeDTO.EmployeeAttendanceDTO> apiRequest, String requestId);
    ApiResponse createUsers(ApiRequest<UserDTO>apiRequest,String requestId);
}

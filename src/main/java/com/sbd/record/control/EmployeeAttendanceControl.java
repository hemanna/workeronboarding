package com.sbd.record.control;

import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;

public interface EmployeeAttendanceControl {
    ApiResponse createAttendance(ApiRequest<EmployeeDTO.EmployeeAttendanceDTO> apiRequest, String requestId);

}

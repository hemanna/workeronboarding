package com.sbd.record.control;

import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;

public interface EmployeeAttendanceControl {
    ApiResponse createAttendance(ApiRequest<EmployeeDTO.EmployeeAttendanceDTO> apiRequest, String requestId);
    ApiResponse updateAttendance(Integer employeeId, ApiRequest<EmployeeDTO> apiRequest, String requestId);
    ApiResponse fetchAttendanceById(Long employeeId, String requestId );
    ApiResponse fetchAllAttendance(String requestId);
    ApiResponse fetchAttendanceByDate(String date, String requestId);
    ApiResponse fetchAttendanceByMonth(String month, String requestId);

}

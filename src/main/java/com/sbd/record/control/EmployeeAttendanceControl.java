package com.sbd.record.control;

import com.sbd.common.Jsonb.EmployeeAttendanceDTO;
import com.sbd.common.Jsonb.EmployeeAttendanceRegularizationJsonb;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;

public interface EmployeeAttendanceControl {
    ApiResponse createAttendance(ApiRequest<EmployeeAttendanceDTO> apiRequest, String requestId);
    ApiResponse updateAttendance(Integer employeeId, ApiRequest<EmployeeDTO> apiRequest, String requestId);
    ApiResponse fetchAttendanceById(Long employeeId, String requestId );
    ApiResponse fetchAllAttendance(String requestId);
    ApiResponse fetchAttendanceByDate(String date, String requestId);
    ApiResponse fetchAttendanceByMonth(String month, String requestId);
    ApiResponse fetchAttendancePending(String requestId);
    ApiResponse fetchAttendanceByRange(String fromDate, String toDate, String requestId);
    ApiResponse deleteAttendance(Long id, String requestId);
    ApiResponse fetchStatusById(Long employeeId, String requestId );
    ApiResponse fetchAttendanceByYearAndMonth(String year, String month, String requestId);
    ApiResponse createRegularization(Integer employeeId, ApiRequest<EmployeeAttendanceRegularizationJsonb> apiRequest, String requestId);



}

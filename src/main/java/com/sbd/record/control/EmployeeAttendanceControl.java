package com.sbd.record.control;

import com.sbd.common.Jsonb.*;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;

public interface EmployeeAttendanceControl {
    ApiResponse createAttendance(ApiRequest<EmployeeAttendanceDTO> apiRequest, String requestId);
    ApiResponse updateAttendance(Integer employeeId, ApiRequest<EmployeeDTO> apiRequest, String requestId);
    ApiResponse fetchAttendanceById(Long employeeId, String requestId );

    ApiResponse fetchAllAttendance(
            String correlationId,
            ApiRequest<EmployeeAttendanceListRequest> apiRequest)
            throws BusinessException;

    ApiResponse fetchOvertimeAttendance(
            String correlationId,
            ApiRequest<EmployeeAttendanceListRequest> apiRequest)
            throws BusinessException;

    ApiResponse fetchAttendanceByDate(String date, String requestId);
    ApiResponse fetchAttendanceByMonth(String month, String requestId);
    ApiResponse fetchAttendancePending(String requestId);
    ApiResponse fetchAttendanceByRange(String fromDate, String toDate, String requestId);
    ApiResponse deleteAttendance(Long id, String requestId);
    ApiResponse fetchStatusById(Long employeeId, String requestId );
    ApiResponse fetchAttendanceByYearAndMonth(String year, String month, String requestId);
    ApiResponse createRegularization(Integer employeeId, ApiRequest<EmployeeAttendanceRegularizationJsonb> apiRequest, String requestId);
    ApiResponse updateRegularizationStatus(Integer regularizationId, String newStatus, String requestId);
    ApiResponse fetchAllRegularizationAttendance(String requestId);
    ApiResponse checkIn(Integer employeeId, ApiRequest<EmployeeAttendanceSessionDTO> apiRequest, String requestId);
    ApiResponse checkOut(Integer employeeId, ApiRequest<EmployeeAttendanceSessionDTO> apiRequest, String requestId);

    ApiResponse lockAttendance(
            ApiRequest<AttendanceLockJsonb> apiRequest,
            String correlationId
    ) throws BusinessException;

    ApiResponse submitAttendance(
            ApiRequest<AttendanceSubmitJsonb> apiRequest,
            String correlationId)
            throws BusinessException;

    ApiResponse fetchAttendanceSummary(
            String correlationId)
            throws BusinessException;

    ApiResponse fetchMonthlyAttendanceTrend(
            ApiRequest<AttendanceTrendRequestJsonb> apiRequest,
            String correlationId)
            throws BusinessException;

    ApiResponse fetchPresentAbsentSummary(
            String correlationId)
            throws BusinessException;
}

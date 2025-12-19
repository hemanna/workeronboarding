package com.sbd.record.boundary;

import com.sbd.common.Jsonb.EmployeeAttendanceDTO;
import com.sbd.common.Jsonb.EmployeeAttendanceListRequest;
import com.sbd.common.Jsonb.EmployeeAttendanceRegularizationJsonb;
import com.sbd.common.Jsonb.EmployeeAttendanceSessionDTO;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.EmployeeAttendanceControl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/employee")
@AllArgsConstructor
@Slf4j
public class EmployeeAttendanceResource {

    private final EmployeeAttendanceControl employeeAttendanceControl;
    @POST
    @Path("/attendance")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse createAttendance(ApiRequest<EmployeeAttendanceDTO> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Create Employee Attendance Request: {}", requestId, apiRequest);
        return employeeAttendanceControl.createAttendance(apiRequest, requestId);
    }


    @PATCH
    @Path("/attendance/{Id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse updateAttendance(@PathParam("Id") Integer Id, ApiRequest<EmployeeDTO> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Update Employee Attendance Request: Id: {}", requestId, Id);
        return employeeAttendanceControl.updateAttendance(Id, apiRequest, requestId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_attendance/{Id}")
    public ApiResponse fetchAttendanceById(@PathParam("Id") Long employeeId) {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | get employee by id: {}", requestId, employeeId);
        return employeeAttendanceControl.fetchAttendanceById(employeeId, requestId);
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_all_attendance")
    public Response fetchAllAttendance(ApiRequest<EmployeeAttendanceListRequest> apiRequest)  throws BusinessException {
        String correlationId = UUID.randomUUID().toString();
        ApiResponse response = employeeAttendanceControl.fetchAllAttendance(correlationId, apiRequest);
        return Response.ok(response).build();
    }

//        @POST
//    @Path("/list")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response assetList(@Context HttpHeaders httpHeaders,
//                              ApiRequest<AssetListRequest> apiRequest) throws BusinessException {
//
//        String correlationId = UUID.randomUUID().toString();
//        ApiResponse response = assetControl.listAssets(correlationId,apiRequest);
//        return Response.ok(response).build();
//    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_attendance_by_date/{date}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse fetchAttendanceByDate(@PathParam("date") String date) {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | get employee attendance by date: {}", requestId, date);
        return employeeAttendanceControl.fetchAttendanceByDate(date, requestId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_attendance/{month}")
    public ApiResponse fetchAttendanceByMonth(@PathParam("month") String month) {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | get employee attendance by month: {}", requestId, month);
        return employeeAttendanceControl.fetchAttendanceByMonth(month, requestId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_attendance/{year}/{month}")
    public ApiResponse fetchAttendanceByYearAndMonth(
            @PathParam("year") String year,
            @PathParam("month") String month
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Fetching attendance for Year: {}, Month: {}", requestId, year, month);
        return employeeAttendanceControl.fetchAttendanceByYearAndMonth(year, month, requestId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_attendance/{year}")
    public ApiResponse fetchAttendanceByYear(
            @PathParam("year") String year
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Fetching attendance for Year: {}", requestId, year);
        return employeeAttendanceControl.fetchAttendanceByYearAndMonth(year, null, requestId);
    }

    @POST
    @Path("/attendance/pending-approvals")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse fetchAttendancePending() {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Fetching pending attendance approvals", requestId);
        return employeeAttendanceControl.fetchAttendancePending(requestId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_attendance_by_range/{fromDate}/{toDate}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse fetchAttendanceByRange(
            @PathParam("fromDate") String fromDate,
            @PathParam("toDate") String toDate) {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | Fetching employee attendance from {} to {}", requestId, fromDate, toDate);
        return employeeAttendanceControl.fetchAttendanceByRange(fromDate, toDate, requestId);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/{id}")
    public ApiResponse deleteAttendance(@PathParam("id") Long id) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Deleting Employee Attendance with ID: {}", requestId, id);
        return employeeAttendanceControl.deleteAttendance(id, requestId);
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/attendance_status/{Id}")
    public ApiResponse fetchStatusById(@PathParam("Id") Long employeeId) {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | get employee by id: {}", requestId, employeeId);
        return employeeAttendanceControl.fetchStatusById(employeeId, requestId);
    }

    @POST
    @Path("/regularization/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse createRegularization(
            @PathParam("employeeId") Integer employeeId,
            ApiRequest<EmployeeAttendanceRegularizationJsonb> apiRequest) {

        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Create Employee Regularization Request: {}, EmployeeId: {}", requestId, apiRequest, employeeId);

        return employeeAttendanceControl.createRegularization(employeeId, apiRequest, requestId);
    }

    @PATCH
    @Path("/regularization/{regularizationId}/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse patchRegularizationStatus(
            @PathParam("regularizationId") Integer regularizationId,
            ApiRequest<String> apiRequest) {

        String requestId = UUID.randomUUID().toString();
        String newStatus = apiRequest.getData();

        log.info("RequestId: {} | PATCH Regularization Status: {}, RegularizationId: {}", requestId, newStatus, regularizationId);

        return employeeAttendanceControl.updateRegularizationStatus(regularizationId, newStatus, requestId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_all_regularization_attendance")
    public ApiResponse fetchAllRegularizationAttendance() {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | fetch All Regularization Attendance ", requestId);
        return employeeAttendanceControl.fetchAllRegularizationAttendance(requestId);
    }

    @POST
    @Path("/attendance/checkin/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse checkIn(@PathParam("employeeId") Integer employeeId,
                               ApiRequest<EmployeeAttendanceSessionDTO> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Check-In Request: employeeId: {}", requestId, employeeId);

        return employeeAttendanceControl.checkIn(employeeId, apiRequest, requestId);
    }

    @POST
    @Path("/attendance/checkout/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse checkOut(@PathParam("employeeId") Integer employeeId,
                                ApiRequest<EmployeeAttendanceSessionDTO> apiRequest) {

        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Check-Out Request: employeeId: {}", requestId, employeeId);

        return employeeAttendanceControl.checkOut(employeeId, apiRequest, requestId);
    }

}

package com.sbd.record.boundary;

import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.EmployeeAttendanceControl;
import com.sbd.record.control.EmployeeRecordControl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
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
    public ApiResponse createAttendance(ApiRequest<EmployeeDTO.EmployeeAttendanceDTO> apiRequest) {
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
    public ApiResponse fetchAllAttendance() {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | get all employee attendance", requestId);
        return employeeAttendanceControl.fetchAllAttendance(requestId);
    }

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
    @Path("/get_attendance_by_month/{month}")
    public ApiResponse fetchAttendanceByMonth(@PathParam("month") String month) {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | get employee attendance by month: {}", requestId, month);
        return employeeAttendanceControl.fetchAttendanceByMonth(month, requestId);
    }

    @POST
    @Path("/attendance/pending-approvals")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse fetchAttendancePending() {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Fetching pending attendance approvals", requestId);
        return employeeAttendanceControl.fetchAttendancePending(requestId);
    }

}

package com.sbd.record.boundary;

import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.LeaveControl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/employee")
@AllArgsConstructor
@Slf4j
public class LeaveResource {

    private final LeaveControl leaveControl;

    @POST
    @Path("/Leave")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse createLeaveRequest(ApiRequest<EmployeeDTO> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Create Employee Leave  Request: {}", requestId, apiRequest);
        return leaveControl.createLeaveRequest(apiRequest, requestId);
    }

    @PATCH
    @Path("/update/{leaveId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse updateLeaveRequest(@PathParam("leaveId") Integer leaveId, ApiRequest<EmployeeDTO> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Update Leave Request: LeaveId: {}", requestId, leaveId);
        return leaveControl.updateLeaveRequest(leaveId, apiRequest, requestId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_leave_request/{Id}")
    public ApiResponse<EmployeeDTO.LeaveDTO> fetchLeaveById(@PathParam("Id") Long Id) {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | get leave by id: {}", requestId, Id);
        return leaveControl.fetchLeaveById(Id, requestId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_all_LeaveRequest")
    public ApiResponse fetchAllLeaveRequest() {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | get all Leave Request", requestId);
        return leaveControl.fetchAllLeaveRequest(requestId);
    }

}

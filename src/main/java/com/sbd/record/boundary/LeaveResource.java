package com.sbd.record.boundary;

import com.sbd.common.Jsonb.LeaveBalanceJsonb;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.LeaveRequest;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.LeaveControl;
import com.sbd.record.control.service.LeaveService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Path("/employee")
@AllArgsConstructor
@Slf4j
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class LeaveResource {
    @Inject
    LeaveService leaveService;
    private final LeaveControl leaveControl;

    @POST
    @Path("/apply")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)

    public Response applyLeave(@BeanParam LeaveRequest leaveRequest) throws BusinessException, IOException {
        String requestId = UUID.randomUUID().toString();
        ApiResponse response = leaveService.createLeaveRequest(leaveRequest, requestId);
        return Response.ok(response).build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get_all_LeaveRequest")
    public Response fetchAllLeaveRequests() {
        String requestId = UUID.randomUUID().toString();
        log.info("Request ID: {} | Fetching all leave requests", requestId);

        ApiResponse apiResponse = leaveControl.fetchAllLeaveRequest(requestId);
        if (apiResponse.getStatus().getStatusCode() == Response.Status.NOT_FOUND.getStatusCode()) {
            return Response.status(Response.Status.NOT_FOUND).entity(apiResponse).build();
        }

        return Response.ok(apiResponse).build();
    }

    @PATCH
    @Path("/approval_status/{leaveId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse updateApprovalStatus(@PathParam("leaveId") Integer leaveId, ApiRequest<EmployeeDTO> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Update Employee Approval Status Request: LeaveId: {}", requestId, leaveId);

        String approvalStatus = apiRequest.getData().getEmployeeDetailsDTO().getApprovalStatus();

        if (approvalStatus == null || approvalStatus.isEmpty()) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Approval status is required", requestId)
            );
        }

        return leaveControl.updateApprovalStatus(leaveId, approvalStatus, requestId);
    }

    @GET
    @Path("/leave_balances/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<List<LeaveBalanceJsonb>> getLeaveBalancesByEmployeeId(
            @PathParam("employeeId") int employeeId,
            @PathParam("year") int year) {
        String requestId = UUID.randomUUID().toString();

        log.info("requestId: {} | Fetch leave balances for employee {} and year {}", requestId, employeeId, year);
        return leaveControl.fetchLeaveBalancesByEmployeeId(employeeId, year, requestId);
    }


}

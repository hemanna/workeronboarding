package com.sbd.record.boundary;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.UserCredentialsDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.EmployeeRecordControl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/employee")
@AllArgsConstructor
@Slf4j
public class EmployeeRecordResource {

    private final EmployeeRecordControl employeeRecordControl;

    @POST
    @Path("/details")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse createEmployeeDetails(ApiRequest<EmployeeDTO> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Create Employee Details Request: {}", requestId, apiRequest);
        return employeeRecordControl.createEmployeeDetails(apiRequest, requestId);
    }
    @PATCH
    @Path("/update/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse updateEmployeeDetails(@PathParam("employeeId") Integer employeeId, ApiRequest<EmployeeDTO> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Update Employee Details Request: EmployeeId: {}", requestId, employeeId);
        return employeeRecordControl.updateEmployeeDetails(employeeId, apiRequest, requestId);
    }

    @PATCH
    @Path("ApprovalStatus/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse updateApprovalStatus(@PathParam("employeeId") Integer employeeId, ApiRequest<EmployeeDTO> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Update Employee Approval Status Request: EmployeeId: {}", requestId, employeeId);
        String approvalStatus = apiRequest.getData().getEmployeeDetailsDTO().getApprovalStatus();
        if (approvalStatus == null || approvalStatus.isEmpty()) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Approval status is required", requestId)
            );
        }
        return employeeRecordControl.updateApprovalStatus(employeeId, approvalStatus, requestId);
    }



    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{employeeId}")
    public ApiResponse fetchEmployeeById(@PathParam("employeeId") Long employeeId) {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | get employee by id: {}", requestId, employeeId);
        return employeeRecordControl.fetchEmployeeById(employeeId, requestId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get_all")
    public ApiResponse fetchAllEmployees() {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | get all employee details", requestId);

        return employeeRecordControl.fetchAllEmployees(requestId);
    }

    @POST
    @Path("/approvals/pending")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse fetchPendingApprovals() {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Fetching pending worker approvals", requestId);
        return employeeRecordControl.fetchPendingApprovals(requestId);
    }

}

package com.sbd.record.boundary;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.*;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.EmployeeRecordControl;
import com.sbd.record.control.service.EmployeeRecordService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestForm;

import java.util.UUID;

@Path("/employee")
@AllArgsConstructor
@Slf4j
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeRecordResource {

    private final EmployeeRecordControl employeeRecordControl;
    private final EmployeeRecordService employeeRecordService;


    @POST
    @Path("/create")
    public Response createEmployeeDetails(
            @BeanParam EmployeeDetailsRequest employeeDetailsRequest)
            throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        ApiResponse response = employeeRecordService.createEmployeeDetails(employeeDetailsRequest, requestId);
        return Response.ok(response).build();
    }




    @POST
    @Path("/update/{employeeId}")
    public Response updateEmployeeDetails(
            @PathParam("employeeId") Integer employeeId,
            @BeanParam EmployeeDetailsRequest employeeDetailsRequest) throws BusinessException {

        String requestId = UUID.randomUUID().toString();
        ApiResponse response = employeeRecordService.updateEmployeeDetails(employeeId, employeeDetailsRequest, requestId);
        return Response.ok(response).build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get_all")
    public ApiResponse fetchAllEmployee(ApiRequest<EmployeeDetailsRequest> apiRequest) throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id: {} | Fetching all employees", requestId);

        return employeeRecordControl.fetchAllEmployee(requestId, apiRequest);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delete_emp/{employeeId}")
    public ApiResponse deleteEmployee(@PathParam("employeeId") Long employeeId) throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id: {} | Deleting employee with ID: {}", requestId, employeeId);

        return employeeRecordControl.deleteEmployee(requestId, employeeId);
    }
//
//    @POST
//    @Path("/approvals/pending")
//    @Produces(MediaType.APPLICATION_JSON)
//    public ApiResponse fetchPendingApprovals() {
//        String requestId = UUID.randomUUID().toString();
//        log.info("RequestId: {} | Fetching pending worker approvals", requestId);
//        return employeeRecordControl.fetchPendingApprovals(requestId);
//    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/skill_count")
    public ApiResponse skillcountEmployee(ApiRequest<EmployeeDetailsRequest> apiRequest) throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id: {} | Fetching all employees", requestId);

        return employeeRecordControl.skillcountEmployee(requestId, apiRequest);
    }
}

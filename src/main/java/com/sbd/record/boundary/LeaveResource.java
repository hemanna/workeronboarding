package com.sbd.record.boundary;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.LeaveRequest;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.LeaveControl;
import com.sbd.record.control.service.LeaveService;
import io.vertx.ext.web.FileUpload;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import java.io.IOException;
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
    public Response fetchAllLeaveRequests() { // Updated method name for clarity
        String requestId = UUID.randomUUID().toString();
        log.info("Request ID: {} | Fetching all leave requests", requestId);

        ApiResponse apiResponse = leaveControl.fetchAllLeaveRequest(requestId);
        if (apiResponse.getStatus().getStatusCode() == Response.Status.NOT_FOUND.getStatusCode()) {
            return Response.status(Response.Status.NOT_FOUND).entity(apiResponse).build();
        }

        return Response.ok(apiResponse).build();
    }

}

package com.sbd.record.boundary;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.UserCredentialsDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.EmployeeRecordControl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
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


}

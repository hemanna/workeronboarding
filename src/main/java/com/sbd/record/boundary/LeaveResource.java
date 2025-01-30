package com.sbd.record.boundary;

import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.LeaveControl;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
}

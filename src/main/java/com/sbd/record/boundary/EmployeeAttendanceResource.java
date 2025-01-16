package com.sbd.record.boundary;

import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.EmployeeAttendanceControl;
import com.sbd.record.control.EmployeeRecordControl;
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
}

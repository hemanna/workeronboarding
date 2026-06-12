package com.sbd.record.boundary;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.DepartmentControl;
import com.sbd.record.control.service.DepartmentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/department")
@AllArgsConstructor
@Slf4j
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentResource {

    @Inject
    DepartmentService departmentService;
    private final DepartmentControl departmentControl;

    @GET
    @Path("/get_department_list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse fetchAllDepartments() throws BusinessException {
        String correlationId = UUID.randomUUID().toString();
        log.info("request_id: {} | Fetching all Departments", correlationId);

        return departmentControl.fetchAllDepartments(correlationId);
    }
}

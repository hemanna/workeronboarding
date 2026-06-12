package com.sbd.record.boundary;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.DepartmentControl;
import com.sbd.record.control.RoleControl;
import com.sbd.record.control.service.DepartmentService;
import com.sbd.record.control.service.RoleService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/role")
@AllArgsConstructor
@Slf4j
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {
    @Inject
    RoleService roleService;
    private final RoleControl roleControl;

    @GET
    @Path("/get_role_list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse fetchAllRoles() throws BusinessException {
        String correlationId = UUID.randomUUID().toString();
        log.info("request_id: {} | Fetching all Roles", correlationId);

        return roleControl.fetchAllRoles(correlationId);
    }
}

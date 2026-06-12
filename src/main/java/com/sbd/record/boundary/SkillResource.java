package com.sbd.record.boundary;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.SkillControl;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/skill")
@AllArgsConstructor
@Slf4j
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class SkillResource {
    @Inject
    SkillControl skillControl;

    @GET
    @Path("/get_skill_list")
    public ApiResponse fetchAllSkills()
            throws BusinessException {

        String correlationId = UUID.randomUUID().toString();

        return skillControl.fetchAllSkills(correlationId);
    }

}

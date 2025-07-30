package com.sbd.common.request;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;
import lombok.Setter;
import org.jboss.resteasy.reactive.PartType;

import java.time.LocalDate;

@Getter
@Setter
public class LeaveRequest {
    @FormParam("employeeId")
    private Integer employeeId;


    @FormParam("leaveTypeId")
    private Integer leaveTypeId;

    @FormParam("departmentId")
    private Integer departmentId;

    @FormParam("startDate")
    private LocalDate startDate;

    @FormParam("endDate")
    private LocalDate endDate;

    @FormParam("reason")
    private String reason;

    @FormParam("attachment")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] attachment;

    @FormParam("attachmentName")
    private String attachmentName;

    @FormParam("adminRemarks")
    private String adminRemarks;

    @FormParam("leaveDuration")
    private String leaveDuration;
}

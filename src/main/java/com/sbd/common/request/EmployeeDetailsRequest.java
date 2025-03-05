package com.sbd.common.request;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;
import lombok.Setter;
import org.jboss.resteasy.reactive.PartType;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeDetailsRequest {
    @FormParam("employeeName")
    private String employeeName;

    @FormParam("guardianName")
    private String guardianName;

    @FormParam("aadhaarNumber")
    private String aadhaarNumber;

    @FormParam("panCard")
    private String panCard;

    @FormParam("dob")
    private LocalDate dob;

    @FormParam("gender")
    private String gender;

    @FormParam("phoneNumber")
    private String phoneNumber;

    @FormParam("emergencyNumber")
    private String emergencyNumber;

    @FormParam("nationality")
    private String nationality;

    @FormParam("bloodGroup")
    private String bloodGroup;

    @FormParam("addressLine1")
    private String addressLine1;

    @FormParam("addressLine2")
    private String addressLine2;

    @FormParam("roleId")
    private Integer roleId;

    @FormParam("state")
    private String state;

    @FormParam("district")
    private String district;

    @FormParam("postalCode")
    private String postalCode;

    @FormParam("experience")
    private Integer experience;

    @FormParam("dateOfJoining")
    private LocalDate dateOfJoining;

    @FormParam("profilePic")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] profilePic;

    @FormParam("aadhaarPic")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] aadhaarPic;

    @FormParam("pancardPic")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] pancardPic;

    @FormParam("status")
    private String status;

    @FormParam("approvalStatus")
    private String approvalStatus;

    @FormParam("departmentId")
    private Integer departmentId;

}

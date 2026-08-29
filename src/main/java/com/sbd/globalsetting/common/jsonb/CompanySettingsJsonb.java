package com.sbd.globalsetting.common.jsonb;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import lombok.*;
import org.jboss.resteasy.reactive.PartType;

@Getter
@Setter
public class CompanySettingsJsonb {

    @FormParam("companyLogo")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] companyLogo;

    @FormParam("companyName")
    private String companyName;

    @FormParam("email")
    private String email;

    @FormParam("phoneNumber")
    private String phoneNumber;

    @FormParam("website")
    private String website;

    @FormParam("gstNumber")
    private String gstNumber;

    @FormParam("panNumber")
    private String panNumber;

    @FormParam("companyAddress")
    private String companyAddress;
}
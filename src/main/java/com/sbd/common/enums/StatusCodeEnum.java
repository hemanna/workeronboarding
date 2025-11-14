package com.sbd.common.enums;

import lombok.Getter;

@Getter
public enum StatusCodeEnum {
    SUCCESS("Success"),
    SUCCESSUPDATED("SuccessFully Updated"),
    TECHNICAL_FAILURE("Technical Failure"),
    BAD_REQUEST("Bad Request"),
    REQUIRED_FIELDS_MISSING("Required Fields Missing"),
    AUTHENTICATION_REQUIRED("Authentication required"),
    NO_CONTENT("No Content"),
    CONFLICT("Duplicate Record"),
    PRECONDITION_FAILED_SAVE("Failed to save record");

    private final String value;

    StatusCodeEnum(String value) {
        this.value = value;
    }
}

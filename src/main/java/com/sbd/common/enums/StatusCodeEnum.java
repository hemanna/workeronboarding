package com.sbd.common.enums;

import lombok.Getter;

@Getter
public enum StatusCodeEnum {
    SUCCESS("Success"),
    TECHNICAL_FAILURE("The system encountered an unexpected error. Please contact support."),
    BAD_REQUEST("Bad Request"),
    REQUIRED_FIELDS_MISSING("Missing required fields. Please provide the required data to proceed."),
    AUTHENTICATION_REQUIRED("Access forbidden: Authentication required."),
    NO_CONTENT("Request successful; no content to return.");

    private final String value;

    StatusCodeEnum(String value) {
        this.value = value;
    }
}

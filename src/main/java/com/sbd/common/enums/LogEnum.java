package com.sbd.common.enums;

import lombok.Getter;

@Getter
public enum LogEnum {

    START_METHOD_REQUEST("correlation-id : {} | method : {} | request : {}"),
    END_METHOD_RESPONSE("correlation-id : {} | status code : {} | message : {}"),
    END_METHOD_DATA_RESPONSE("correlation-id : {} | status code : {} | message : {} | response : {}"),
    END_METHOD_FAILURE("correlation-id : {} | status code : {} | message : {} | Error : {}"),
    EXCEPTION_FAILURE("correlation-id : {} | message : {} | cause : {}"),
    ACTIVITY("correlation-id : {} | activity : {} | message : {}"),
    ACTIVITY_ONE("correlation-id : {} | statusCode : {} | activity : {} | message : {}"),
    HTTP_CLIENT_REQUEST("Http Client Request correlation-id : {} | http-request-id : {} | uri : {} | payload : {}"),
    HTTP_CLIENT_RESPONSE("Http Client Response correlation-id : {} | http-request-id : {} | statusCode : {} | body : {}"),
    ACTIVITY_RESPONSE_TIME("correlation-id : {} | activity : {} | message : {} | time : {}"),
    ACTIVITY_METHOD("correlation-id : {} | activity : {} | message: {} | method : {} | payload : {}"),
    ASYNCHRONOUSLY_EXCEPTION("Error processing asynchronously correlation-id : {} | cause : {}");

    @Getter
    public enum LogMessage {
        STARTED("Started"),
        ENDED("Ended");
        private final String value;

        LogMessage(String value){
            this.value = value;
        }
    }
    private final String value;

    LogEnum(String value) {
        this.value = value;
    }
}

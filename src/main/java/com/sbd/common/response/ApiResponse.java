package com.sbd.common.response;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({"status", "data"})
public class ApiResponse {

    private Status status;
    private Object data;

    public ApiResponse(Status status) {
        this.status = status;
    }


}

package com.sbd.common.response;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({"status", "data"})
public class ApiResponse<T> {

    private Status status;
    private T data;

    public ApiResponse(Status status) {
        this.status = status;
    }
}

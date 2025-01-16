package com.sbd.common.request;

import lombok.Data;

@Data
public class UserCredentialsDTO {
    private Integer id;
    private String username;
    private String password;
    private Integer employeeId;
}

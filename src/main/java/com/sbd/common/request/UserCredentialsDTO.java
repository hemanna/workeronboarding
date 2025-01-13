package com.sbd.common.request;

import lombok.Data;

@Data
public class UserCredentialsDTO {
    private Integer id;
    private String username;
    private String password;     // Password for the login or reset functionality
    private Integer employeeId;  // Associated employee's ID
}

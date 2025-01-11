package com.sbd.common.request;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String email;
    private String phoneNumber;
    private String password;

}

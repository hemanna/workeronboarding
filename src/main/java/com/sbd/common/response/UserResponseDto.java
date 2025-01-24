package com.sbd.common.response;

import lombok.Data;

@Data
public  class UserResponseDto {

        private Integer employeeId;
        private String employeeName;
        private Integer roleId;
        private String roleName;
        private String departmentName;
}

package com.sbd.common.Jsonb;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String userName;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}

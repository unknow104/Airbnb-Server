package com.techpower.airbnb.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResetPasswordWithOTPRequest {
    private String email;
    private String otpCode;
    private String newPassword;
}

package com.techpower.airbnb.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyOTPRequest {
    String email;
    String OtpCode;
}

package com.techpower.airbnb.api;

import com.techpower.airbnb.auth.AuthenticationResponse;
import com.techpower.airbnb.auth.AuthenticationService;
import com.techpower.airbnb.request.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response);
    }

    @PostMapping("register-customer")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterCustomerRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("register-owner")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterOwnerRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PutMapping(path = "confirm")
    public String confirm(@RequestParam("email") String email, @RequestParam("token") String token) {
        return authenticationService.confirmToken(email, token);
    }
    @DeleteMapping("delete/{email}")
    public ResponseEntity<?> delete(@PathVariable String email) {
        authenticationService.delete(email);
        return ResponseEntity.status(204).build();
    }
    @PostMapping("/send-otp-for-password-reset")
    public ResponseEntity<?> sendOTPForPasswordReset(@RequestBody ForgotPasswordRequest request) {
        return authenticationService.sendOTPForPasswordReset(request.getEmail());
    }

    @PostMapping("/verify-otp-for-password-reset")
    public ResponseEntity<?> verifyOTPForPasswordReset(@RequestBody VerifyOTPRequest request) {
        return authenticationService.verifyOTPForPasswordReset(request.getEmail(), request.getOtpCode());
    }

    @PostMapping("/reset-password-with-otp")
    public ResponseEntity<?> resetPasswordWithOTP(@RequestBody ResetPasswordWithOTPRequest request) {
        return authenticationService.resetPasswordWithOTP(request);
    }
}





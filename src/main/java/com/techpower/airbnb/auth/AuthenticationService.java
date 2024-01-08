package com.techpower.airbnb.auth;


import com.techpower.airbnb.constant.Role;
import com.techpower.airbnb.constant.Status;
import com.techpower.airbnb.converter.UserDTOMapper;
import com.techpower.airbnb.dto.UserDTO;
import com.techpower.airbnb.entity.UserEntity;
import com.techpower.airbnb.jwt.JWTUtil;
import com.techpower.airbnb.repository.UserRepository;
import com.techpower.airbnb.request.*;
import com.techpower.airbnb.service.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDTOMapper userDTOMapper;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final UserRepository userRepository;

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        UserEntity principal = (UserEntity) authentication.getPrincipal();
        UserDTO userDTO = userDTOMapper.apply(principal);
        if (principal.getStatus().equals(Status.ACTIVE)) {
            String token = jwtUtil.issueToken(userDTO.userName(), userDTO.role());
            return new AuthenticationResponse(token, userDTO);
        } else return new AuthenticationResponse("Status: " + principal.getStatus(), userDTO);
    }

    public AuthenticationResponse register(RegisterCustomerRequest request) {

        boolean userExists = userRepository
                .findByEmail(request.getEmail())
                .isPresent();

        String codeConfirmed = generateRandomString();
        if (userExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.
            throw new IllegalStateException("Email already taken");
        }

        var user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .birthday(request.getBirthday())
                .gender(request.isGender())
                .role(Role.CUSTOMER)
                .status(Status.ACTIVE)
                .confirmed(false)
                .codeConfirmed(codeConfirmed)
                .build();
        userRepository.save(user);


        String token = jwtUtil.issueToken(user.getUsername(), user.getRole().toString());

        emailSender.send(
                request.getEmail(),
                buildEmail(request.getName(), codeConfirmed),
                "Confirm your email");

        return AuthenticationResponse.builder()
                .token(token)
                .userDTO(userDTOMapper.apply(user))
                .build();
    }

    public AuthenticationResponse register(RegisterOwnerRequest request) {
        String codeConfirmed = generateRandomString();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiryTime = currentTime.plusMinutes(15); // Thời gian hết hạn sau 15 phút

        var user = UserEntity.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.OWNER)
                .status(Status.ACTIVE)
                .confirmed(false)
                .codeConfirmed(codeConfirmed)
                .otpExpiryTime(expiryTime)
                .build();
        userRepository.save(user);

        String token = jwtUtil.issueToken(user.getUsername(), user.getRole().toString());

        emailSender.send(
                request.getEmail(),
                buildEmail(request.getName(), codeConfirmed),
                "Confirm your email");

        return AuthenticationResponse.builder()
                .token(token)
                .userDTO(userDTOMapper.apply(user))
                .build();
    }
    @Transactional
    public String confirmToken(String email, String token) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getCodeConfirmed().equals(token)) {
                userRepository.updateConfirmedByEmailAndCodeConfirmed(true, email, token);
                return "confirmed";
            }
        }
        return "not confirmed";
    }

    public String generateRandomString() {
        char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHARACTERS.length);
            sb.append(CHARACTERS[index]);
        }
        return sb.toString();
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"text-align: center; margin: 0 auto; max-width: 600px; padding: 20px; border: 1px solid #ccc; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">\n" +
                "  <img src=\"https://res.cloudinary.com/drn7nawnc/image/upload/v1687899742/unnamed_gssfpv.png\" alt=\"Logo\" style=\"max-width: 50%;\">\n" +
                "  <h2 style=\"font-size: 24px; margin-bottom: 10px;\">Hi " + name + ",</h2>" +
                "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. To complete the registration, enter the verification code below on the website to continue the experience: </p>" +
                "<blockquote style=\"Margin:0 0 20px 0;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">" +
                "  <h2 style=\"Margin:0 0 20px 0;font-size: 24px; margin-bottom: 10px;color: darkblue;background: gainsboro;\">" + link + "</h2>" +
                "</blockquote>" +
                "<p style=\"font-size: 16px; margin-bottom: 20px;\">Link will expire in 15 minutes. </p>" +
                "<p style=\"font-size: 16px; margin-bottom: 20px;\">See you soon</p>" +
                "</div>";
    }

    // Phương thức để tạo mã OTP ngẫu nhiên
    private String generateRandomOTP() {
        int otpLength = 6;
        StringBuilder otp = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < otpLength; i++) {
            int digit = random.nextInt(10); // Tạo một số ngẫu nhiên từ 0 đến 9
            otp.append(digit);
        }

        return otp.toString();
    }

    // Phương thức để kiểm tra mã OTP có hợp lệ
    private boolean isOtpValid(UserEntity user, String otpCode) {
        if (user.getCodeConfirmed() != null &&
                user.getOtpExpiryTime() != null &&
                LocalDateTime.now().isBefore(user.getOtpExpiryTime()) &&
                user.getCodeConfirmed().equals(otpCode)) {
            return true;
        }
        return false;
    }

    public ResponseEntity<?> verifyOTPForPasswordReset(String email, String otpCode) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // Kiểm tra xem mã OTP có hợp lệ không
            if (isOtpValid(user, otpCode)) {
                return ResponseEntity.ok("Mã OTP hợp lệ.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP không hợp lệ.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng với email này.");
        }
    }

    public ResponseEntity<?> resetPasswordWithOTP(ResetPasswordWithOTPRequest request) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // Kiểm tra xem mã OTP có hợp lệ không
            if (isOtpValid(user, request.getOtpCode())) {
                // Mã OTP hợp lệ, cho phép người dùng đặt lại mật khẩu
                String newPassword = request.getNewPassword();
                user.setPassword(passwordEncoder.encode(newPassword));

                // Xóa mã OTP sau khi đặt lại mật khẩu
                user.setCodeConfirmed(null);
                user.setOtpExpiryTime(null);

                // Lưu UserEntity vào cơ sở dữ liệu
                userRepository.save(user);

                return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP không hợp lệ.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng với email này.");
        }
    }

    public ResponseEntity<?> sendOTPForPasswordReset(String email) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // Tạo mã OTP ngẫu nhiên
            String otpCode = generateRandomOTP();

            // Lưu mã OTP vào cơ sở dữ liệu và thiết lập thời gian hết hạn
            user.setCodeConfirmed(otpCode);
            user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(15)); // Hết hạn sau 15 phút
            userRepository.save(user);

            // Gửi mã OTP đến email của người dùng
            emailSender.send(
                    user.getEmail(),
                    "Mã OTP của bạn để đặt lại mật khẩu: " + otpCode,
                    "Đặt lại mật khẩu OTP"
            );

            return ResponseEntity.ok("Mã OTP đã được gửi đến email của bạn.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng với email này.");
        }
    }

    @Transactional
    public String delete(String email) {
        userRepository.deleteByEmail(email);
        return "deleted";
    }
}

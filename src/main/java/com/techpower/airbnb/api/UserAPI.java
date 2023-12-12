package com.techpower.airbnb.api;

import com.techpower.airbnb.constant.Status;
import com.techpower.airbnb.dto.UserDTO;
import com.techpower.airbnb.entity.UserEntity;
import com.techpower.airbnb.service.IUserService;
import com.techpower.airbnb.service.impl.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class UserAPI {

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private IUserService userService;


    @GetMapping("/customer")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        return ResponseEntity.ok(userService.getAllCustomer());
    }

    @GetMapping("/owner")
    public ResponseEntity<List<UserDTO>> getAllOwner() {
        return ResponseEntity.ok(userService.getAllOwner());
    }

    @PutMapping("/{idUser}/status/{status}")
    public ResponseEntity<UserDTO> lock(@PathVariable("idUser") long idUser,
                                        @PathVariable("status") String status) {
        UserDTO deletedUser = userService.lock(idUser, status);
        if (deletedUser != null) {
            return ResponseEntity.ok(deletedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<?> getInformation(@PathVariable("idUser") Long idUser) {
        return ResponseEntity.ok(userService.getInformation(idUser));
    }

    @GetMapping("/{idUser}/orders")
    public ResponseEntity<?> findAllOrders(@PathVariable("idUser") long idUser) {
        return ResponseEntity.ok(userService.findAllOrders(idUser));
    }

    @GetMapping("/{idUser}/manager-orders")
    public ResponseEntity<?> getAllOrdersByOwner(@PathVariable("idUser") Long idUser) {
        return ResponseEntity.ok(userService.getAllOrdersByOwner(idUser));
    }

    @GetMapping("/{idUser}/rooms")
    public ResponseEntity<?> findAllRooms(@PathVariable("idUser") long idUser) {
        return ResponseEntity.ok(userService.findAllRooms(idUser));
    }

    @GetMapping("/{idUser}/bookings-date")
    public ResponseEntity<?> findAllBookingsDate(@PathVariable("idUser") Long idUser) {
        return ResponseEntity.ok(userService.findAllBookingsDate(idUser));
    }

    @GetMapping("/{idUser}/feedback")
    public ResponseEntity<?> findAllFeedback(@PathVariable("idUser") Long idUser) {
        return ResponseEntity.ok(userService.findAllFeedbackByCustomer(idUser));
    }

    @GetMapping("/{idUser}/manager-feedback")
    public ResponseEntity<?> getAllFeedbackByOwner(@PathVariable("idUser") Long idUser) {
        return ResponseEntity.ok(userService.getAllFeedbackByOwner(idUser));
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<?> updateStatus(
            @PathVariable("idUser") long idUser,
            @RequestParam("status") Status status
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateStatus(status, idUser));
    }

    @PutMapping("/profile/{idUser}")
    public ResponseEntity<?> updateProfile(
            @PathVariable("idUser") Long idUser,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("birthday") String birthday,
            @RequestParam("gender") boolean gender,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            UserEntity existUser = userService.getOne(idUser);
            if (existUser != null) {
                UserEntity userEntity = UserEntity.builder()
                        .id(idUser)
                        .name(name)
                        .phone(phone)
                        .email(email)
                        .birthday(birthday)
                        .gender(gender)
                        .image(cloudinaryService.uploadImage(image))
                        .build();
                UserEntity userEntitySave = userService.update(userEntity);
                return ResponseEntity.status(HttpStatus.OK).body(userEntitySave);

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng này");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi tạo dịch vụ.");
        }
    }

    @PutMapping("/host/{idUser}")
    public ResponseEntity<?> updateRole(
            @PathVariable("idUser") long idUser
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateToHost(idUser));
    }
}

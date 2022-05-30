package uz.example.flower.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.db.LoginDto;
import uz.example.flower.model.dto.db.RegisterDto;
import uz.example.flower.payload.request.ChangePasswordDto;
import uz.example.flower.payload.request.UserUpdateDto;
import uz.example.flower.service.UserService;
import javax.validation.Valid;

@RestController
@RequestMapping("api/secure/")
public class AuthSecureController {
    private final UserService userService;

    public AuthSecureController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto login) {
        JSend send = userService.signIn(login);
        return ResponseEntity.ok(send);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterDto register) {
        JSend user = userService.saveUser(register);
        return ResponseEntity.ok(user);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateData(@RequestBody @Valid UserUpdateDto userUpdate) {
        JSend response = userService.updateUser(userUpdate);
        return ResponseEntity.ok(response);
    }

    @PostMapping("reset_password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordDto passwordDto) {
        JSend response = userService.changePassword(passwordDto);
        return ResponseEntity.ok(response);
    }
}

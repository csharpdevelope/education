package uz.example.flower.controller;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.db.CheckCode;
import uz.example.flower.model.dto.db.LoginDto;
import uz.example.flower.model.dto.db.RegisterDto;
import uz.example.flower.payload.request.ChangePasswordDto;
import uz.example.flower.payload.request.UserUpdateDto;
import uz.example.flower.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        return new ResponseEntity<>(send, HttpStatus.valueOf(send.getCode()));
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterDto register) {
        JSend user = userService.saveUser(register);
        return new ResponseEntity<>(user, HttpStatus.valueOf(user.getCode()));
    }

    @PostMapping("code_check")
    public ResponseEntity<?> codeCheck(@RequestBody CheckCode checkCode) {
        return null;
    }
    
    @PostMapping("logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextHolder = new SecurityContextLogoutHandler();
        securityContextHolder.logout(request, response, null);
    }

    @PostMapping("refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody String refresh_token) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("update")
    public ResponseEntity<?> updateData(@RequestBody @Valid UserUpdateDto userUpdate) {
        JSend response = userService.updateUser(userUpdate);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PostMapping("reset_password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordDto passwordDto) {
        JSend response = userService.changePassword(passwordDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}

package uz.example.flower.controller;

import  org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import uz.example.flower.model.JSend;
import uz.example.flower.payload.request.AdminUserDto;
import uz.example.flower.payload.response.UserDto;
import uz.example.flower.service.UserService;
import uz.example.flower.component.SecurityUtils;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class UserController {
    private final UserService userService;
    private final SecurityUtils securityUtils;

    public UserController(UserService userService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("get/user")
    public ResponseEntity<?> getUserData() {
        UserDto response = userService.getUser(securityUtils.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @PostMapping("add/role")
    @Secured("ADMIN")
    public ResponseEntity<?> addRoleToUser(@RequestParam(value = "username") String username,
                                           @RequestParam(value = "role_name") List<String> roleNames) {
        JSend response = userService.addRoleToUser(username, roleNames);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PostMapping("delete/role")
    @Secured("ADMIN")
    public ResponseEntity<?> deleteRole(@RequestParam(value = "username") String username,
                                        @RequestParam(value = "role_name") String role_name) {
        JSend response = userService.deleteRoleFromUser(username, role_name);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PostMapping("add/user")
    @Secured("ADMIN")
    public ResponseEntity<?> addAdmin(@RequestBody AdminUserDto userDto) {
        JSend response;
        if (userDto.getUsername() == null) {
            response = JSend.badRequest("Username valid null");
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
        }
        response = userService.addRoleToUser(userDto.getUsername(), userDto.getRoles());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}

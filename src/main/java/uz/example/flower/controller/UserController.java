package uz.example.flower.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.example.flower.model.JSend;
import uz.example.flower.payload.request.AdminUserDto;
import uz.example.flower.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("add/role")
    public ResponseEntity<?> addRoleToUser(@RequestParam(value = "username") String username,
                                           @RequestParam(value = "role_name") List<String> roleNames) {
        JSend response = userService.addRoleToUser(username, roleNames);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PostMapping("delete/role")
    public ResponseEntity<?> deleteRole(@RequestParam(value = "username") String username,
                                        @RequestParam(value = "role_name") String role_name) {
        JSend response = userService.deleteRoleFromUser(username, role_name);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PostMapping("add/user")
    public ResponseEntity<?> addAdmin(@RequestBody AdminUserDto userDto) {
        return ResponseEntity.ok().build();
    }
}

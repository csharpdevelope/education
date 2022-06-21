package uz.example.flower.service;

import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.db.LoginDto;
import uz.example.flower.model.dto.db.RegisterDto;
import uz.example.flower.model.entity.User;
import uz.example.flower.payload.request.ChangePasswordDto;
import uz.example.flower.payload.response.UserDto;

import java.util.List;

public interface UserService {
    JSend saveUser(RegisterDto user);

    JSend signIn(LoginDto login);

    User findById(Long userId);

    JSend changePassword(ChangePasswordDto passwordDto);

    UserDto getUser(User currentUser);
}
package uz.example.flower.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.example.flower.config.security.CustomUserDetails;
import uz.example.flower.config.security.jwt.JwtTokenProvider;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.db.LoginDto;
import uz.example.flower.model.dto.db.RegisterDto;
import uz.example.flower.model.entity.Role;
import uz.example.flower.model.entity.User;
import uz.example.flower.model.enums.RoleEnum;
import uz.example.flower.payload.request.AdminUserDto;
import uz.example.flower.payload.request.ChangePasswordDto;
import uz.example.flower.payload.request.UserUpdateDto;
import uz.example.flower.payload.response.SignInDto;
import uz.example.flower.payload.response.UserDto;
import uz.example.flower.repository.RoleRepository;
import uz.example.flower.repository.UserRepository;
import uz.example.flower.service.UserService;
import uz.example.flower.service.tools.SecurityUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository  userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final SecurityUtils securityUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.securityUtils = securityUtils;
    }

    @Override
    public JSend saveUser(RegisterDto register) {
        if (userRepository.existsByEmail(register.getEmail()) || userRepository.existsByUsername(register.getUsername())) {
            return JSend.badRequest(406, "User with given email/username already exists");
        }
        User user = new User();
        user.setEmail(register.getEmail());
        user.setFirstname(register.getFirstname());
        user.setUsername(register.getUsername());
        user.setLastname(register.getLastname());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        Role role = roleRepository.findByName(RoleEnum.USER);
        user.getRoles().add(role);
        userRepository.save(user);
        UserDto userDto = user.toUserDto();
        return JSend.success(userDto);
    }

    @Override
    public JSend signIn(LoginDto login) {
        User user = userRepository.findByUsername(login.getUsername());
        if (user == null) {
            return JSend.notFound(401,"Username or Password invalid");
        }

        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())){
            return JSend.notFound(401, "Username or Password invalid");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        if (customUserDetails != null) {
            user = customUserDetails.getUser();
            Timestamp issuerAt = new Timestamp(System.currentTimeMillis());
            String accessToken = jwtTokenProvider.generateToken(user, issuerAt);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user);
            SignInDto signIn = new SignInDto(accessToken, refreshToken, user.getId());
            return JSend.success(signIn);
        }
        return JSend.notFound(401,"Username or Password invalid");
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("no authorization"));
    }

    @Override
    public JSend addRoleToUser(String username, List<String> roleName) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return JSend.notFound("User not found");
        }
        List<RoleEnum> roleEnums = new ArrayList<>();
        roleName.forEach(role -> roleEnums.add(RoleEnum.valueOf(role)));
        Set<Role> roles = roleRepository.findAllByNameIn(roleEnums);
        user.getRoles().addAll(roles);
        userRepository.save(user);
        return JSend.success("Role add successfully");
    }

    @Override
    public JSend deleteRoleFromUser(String username, String role_name) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return JSend.notFound("User not found");
        }
        Role role = roleRepository.findByName(RoleEnum.valueOf(role_name));
        if (role == null) {
            return JSend.notFound("Role not found");
        }
        Set<Role> roles = user.getRoles();
        if(roles.contains(role)) {
            boolean isDelete = roles.remove(role);
            user.setRoles(roles);
            userRepository.save(user);
            if (isDelete) {
                return JSend.success("The role was successfully removed");
            }

            return JSend.fail("The role does not remove");
        }

        return JSend.notFound("The user does not have such a role");
    }

    @Override
    public JSend updateUser(UserUpdateDto userUpdate) {
        User user = securityUtils.getCurrentUser();
        if (passwordEncoder.matches(user.getPassword(), userUpdate.getPassword())) {
            return JSend.badRequest("Password incorrect");
        }
        user.setUsername(userUpdate.getUsername());
        user.setEmail(userUpdate.getEmail());
        return JSend.success("User changed successfully");
    }

    @Override
    public JSend changePassword(ChangePasswordDto passwordDto) {
        User user = userRepository.findByUsername(passwordDto.getUsername());
        if (user == null) {
            return JSend.notFound("User " + passwordDto.getUsername() + " not found");
        }

        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            return JSend.badRequest("Password and Confirmation Password incorrect");
        }

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        return JSend.success("Password changed successfully");
    }
}

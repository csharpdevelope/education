package uz.example.flower.service.tools;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.example.flower.config.security.CustomUserDetails;
import uz.example.flower.model.entity.User;
import uz.example.flower.repository.UserRepository;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            user = userRepository.findByUsername(customUserDetails.getUsername());
        }

        return user;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

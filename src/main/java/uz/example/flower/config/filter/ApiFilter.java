package uz.example.flower.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.example.flower.config.security.CustomUserDetails;
import uz.example.flower.config.security.jwt.JwtTokenProvider;
import uz.example.flower.model.entity.User;
import uz.example.flower.service.UserService;
import uz.example.flower.utils.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class ApiFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    public ApiFilter(JwtTokenProvider jwtTokenProvider, @Lazy UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJwtToken(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtTokenProvider.validateToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            User user = userService.findById(jwtTokenProvider.getUserId(token));
            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails.getUsername(), null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtToken(HttpServletRequest request) {
        String header = request.getHeader(Constants.AUTHORIZATION);

        if (header != null && header.startsWith(Constants.BEARER)) {
            return header.substring(7);
        }
        return null;
    }
}

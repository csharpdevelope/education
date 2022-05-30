package uz.example.flower.config.security.core;

public class SecurityConstant {
    public static final String[] ACCESS_URL_TOKEN = {
            "/api/secure/register",
            "/api/secure/login",
            "/api/secure/reset_password",
            "/api/secure/code_check"
    };

    public static final String[] FLOWER_URL = {
            "/api/v1/flowers/**",
            "/api/v1/category/**"
    };
}

package uz.example.flower.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.example.flower.model.entity.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenProvider {
    @Value("${jwt.token.access.key}")
    private String accessKey;
    @Value("${jwt.token.access.expire}")
    private Long accessExpire;
    @Value("${jwt.token.refresh.key}")
    private String refreshKey;
    @Value("${jwt.token.refresh.expire}")
    private Long refreshExpire;

    public String generateToken(User user, Timestamp issuerAt) {
        Timestamp expireAt = new Timestamp(System.currentTimeMillis() + accessExpire);
        String userId = String.valueOf(user.getId());
        Algorithm algorithm = Algorithm.HMAC256(accessKey.getBytes());
        List<String> roles = new ArrayList<>();
        user.getRoles().forEach(role -> roles.add(role.getName().name()));
        String accessToken = JWT.create()
                .withSubject(userId)
                .withIssuedAt(issuerAt)
                .withExpiresAt(expireAt)
                .withClaim("roles", roles)
                .sign(algorithm);

        return accessToken;
    }

    public boolean validateToken(String token) {
        DecodedJWT decode = decodeJwtToken(token);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp expireAt = new Timestamp(decode.getExpiresAt().getTime());
        if (expireAt.before(currentTime)) {
            return false;
        }
        return true;
    }

    private DecodedJWT decodeJwtToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(accessKey.getBytes());
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT decode = verifier.verify(token);
        return decode;
    }


    public String generateRefreshToken(User user) {
        Timestamp issuerAt = new Timestamp(System.currentTimeMillis());
        Timestamp expireAt = new Timestamp(System.currentTimeMillis() + refreshExpire);
        Algorithm algorithm = Algorithm.HMAC256(refreshKey.getBytes());
        String refreshToken = JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withIssuedAt(issuerAt)
                .withExpiresAt(expireAt)
                .sign(algorithm);

        return refreshToken;
    }

    public Long getUserId(String token) {
        DecodedJWT decode = decodeJwtToken(token);
        Long userId = Long.valueOf(decode.getSubject());
        return userId;
    }
}

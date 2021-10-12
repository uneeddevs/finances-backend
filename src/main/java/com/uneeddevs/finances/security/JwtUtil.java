package com.uneeddevs.finances.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.uneeddevs.finances.model.Profile;
import com.uneeddevs.finances.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user){
        Algorithm algorithm = Algorithm.HMAC512(secret);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .withIssuer("com.uneeddevs")
                .withClaim("id", user.getEmail())
                .withClaim("roles", "[" + user.getProfiles()
                        .stream()
                        .map(Profile::getRoleName)
                        .collect(Collectors.joining(","))
                    + "]"
                )
                .sign(algorithm);
    }

    public boolean isValidToken(String token){
        DecodedJWT decodedJWT = getDecodedJWT(token);

        if(Objects.nonNull(decodedJWT)){
            String username = decodedJWT.getSubject();
            Date expirationDate = decodedJWT.getExpiresAt();
            Date now = new Date(System.currentTimeMillis());
            return Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate);
        }
        return false;
    }

    public String getUsername(String token){
        DecodedJWT decodedJWT = getDecodedJWT(token);
        if(Objects.nonNull(decodedJWT))
            return decodedJWT.getSubject();
        return null;
    }

    public Long getExpiration(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        if(Objects.nonNull(decodedJWT))
            return decodedJWT.getExpiresAt().getTime();
        return 0L;
    }

    private DecodedJWT getDecodedJWT(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .withIssuer("com.uneeddevs")
                    .build();
            return verifier.verify(token);
        } catch (Exception e){
            return null;
        }
    }

}

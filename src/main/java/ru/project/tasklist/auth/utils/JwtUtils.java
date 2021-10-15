package ru.project.tasklist.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.project.tasklist.auth.entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Component
@Log
public class JwtUtils {
    public static final String CLAIM_USER_KEY = "user";
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access_token-expiration}")
    private int accessTokenExpiration;

    public String createAccessToken(User user) {
        Date currentDate = new Date();

        Map claims = new HashMap<String, String[]>();
        claims.put(CLAIM_USER_KEY, user);
        claims.put(Claims.SUBJECT, user.getId());

        return Jwts.builder()
              //  .setSubject(user.getId().toString())
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + accessTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validate(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException e) {
            log.log(Level.SEVERE, "Invalid Jwt Token: " + jwt);
        } catch (ExpiredJwtException e) {
            log.log(Level.SEVERE, "Invalid Jwt expiried: " + jwt);
        } catch (UnsupportedJwtException e) {
            log.log(Level.SEVERE, "Invalid Jwt Unsupported: " + jwt);
        } catch (IllegalArgumentException e) {
            log.log(Level.SEVERE, "JWT claims string is empry: " + jwt);
        }

        return false;
    }

    public User getUser(String jwt) {
        Map map = (Map)Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().get("user");

        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(map, User.class);

        return user;
    }
}

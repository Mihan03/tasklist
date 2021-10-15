package ru.project.tasklist.auth.utils;

import lombok.extern.java.Log;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Component
@Log
public class CookieUtils {
    private final String ACCESS_TOKEN = "access_token";

    @Value("${cookie.jwt.max-age}")
    private int cookieAccessTokenDuration;

    @Value("${cookie.domain}")
    private String cookieAccessTokenDomain;


    public HttpCookie createJwtCookie(String jwt) {
        return ResponseCookie
                .from(ACCESS_TOKEN, jwt)
                .maxAge(cookieAccessTokenDuration)
                .sameSite(SameSiteCookies.STRICT.getValue())
                .httpOnly(true)
                .secure(true)
                .domain(cookieAccessTokenDomain)
                .path("/")
                .build();
    }

    public String getCookieAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ACCESS_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

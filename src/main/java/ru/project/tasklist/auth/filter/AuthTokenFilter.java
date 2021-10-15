package ru.project.tasklist.auth.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.project.tasklist.auth.entity.User;
import ru.project.tasklist.auth.exception.JwtCommonException;
import ru.project.tasklist.auth.service.UserDetailsImpl;
import ru.project.tasklist.auth.utils.CookieUtils;
import ru.project.tasklist.auth.utils.JwtUtils;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
@Getter
@Setter
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private CookieUtils cookieUtils;

    private List<String> permitUrl = Arrays.asList(
            "register",
            "login",
            "activate-account",
            "resend-activate-email",
            "send-reset-password-email",
            "test-no-auth",
            "index"
    );

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setCookieUtils(CookieUtils cookieUtils) {
        this.cookieUtils = cookieUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        boolean isRequestToPublicApi = permitUrl.stream().anyMatch(s -> request.getRequestURI().toLowerCase().contains(s));

        if (!isRequestToPublicApi
                //&& SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            String jwt = cookieUtils.getCookieAccessToken(request);

            if (jwt != null) {
                if (jwtUtils.validate(jwt)) {
                    System.out.println("jwt = " + jwt);

                    User user = jwtUtils.getUser(jwt);

                    UserDetailsImpl userDetails = new UserDetailsImpl(user);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new JwtCommonException("jwt validate exception");
                }
            } else {
                throw new AuthenticationCredentialsNotFoundException("token not found");
            }
        }



        filterChain.doFilter(request, response);
    }
}

package ru.project.tasklist.auth.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.project.tasklist.auth.entity.Activity;
import ru.project.tasklist.auth.entity.Role;
import ru.project.tasklist.auth.entity.User;
import ru.project.tasklist.auth.exception.RoleNotFoundException;
import ru.project.tasklist.auth.exception.UserAlreadyActivatedException;
import ru.project.tasklist.auth.exception.UserOrEmailExistsException;
import ru.project.tasklist.auth.objects.JsonException;
import ru.project.tasklist.auth.service.UserDetailsImpl;
import ru.project.tasklist.auth.service.UserService;
import ru.project.tasklist.auth.utils.CookieUtils;
import ru.project.tasklist.auth.utils.JwtUtils;

import javax.validation.Valid;
import java.util.UUID;

import static ru.project.tasklist.auth.service.UserService.DEFAULT_ROLE;

@RestController
@RequestMapping("/auth")
@Log
public class AuthController {

    private UserService userService; // сервис для работы с пользователями
    private PasswordEncoder encoder; // кодировщик паролей (или любых данных), создает односторонний хеш
    private AuthenticationManager authenticationManager; // стандартный встроенный менеджер Spring, проверяет логин-пароль
    private JwtUtils jwtUtils;
    private CookieUtils cookieUtils;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, CookieUtils cookieUtils) {
        this.userService = userService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.cookieUtils = cookieUtils;
    }

    @PutMapping("/register")
    public ResponseEntity register(@Valid @RequestBody User user) {

        if (userService.userExists(user.getUsername(), user.getEmail())) {
            throw new UserOrEmailExistsException("User or email already exists");
        }

        Role userRole = userService.findByName(DEFAULT_ROLE).
                orElseThrow(() -> new RoleNotFoundException("Default Role User not found"));
        user.getRoles().add(userRole);

        user.setPassword(encoder.encode(user.getPassword()));



        Activity activity = new Activity();
        activity.setUser(user);
        activity.setUuid(UUID.randomUUID().toString());

        userService.register(user, activity);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate-account")
    public ResponseEntity<Boolean> activateUser(@RequestBody String uuid) {
        Activity activity = userService.findActivityByUiid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("Activity not found with uuid: " + uuid));

        if (activity.isActivated()) {
            throw new UserAlreadyActivatedException("User already activated");
        }

        int updatedCount = userService.activate(uuid);

        return ResponseEntity.ok(updatedCount == 1);
    }

    @ExceptionHandler
    public ResponseEntity<JsonException> handleExceptions(Exception ex) {
        return new ResponseEntity(new JsonException(ex.getClass().getSimpleName()), HttpStatus.BAD_REQUEST);
    }

    //залогиниться по паролю-пользователю
    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody User user) {

        //проверка логина-пароля
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        //добавляем в спринг контейнер информацию об авторизации
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //UserDetailsImpl - спец объект, который хранится в Спринг контейнере и содержит данные пользователя
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.isActivated()) {
            throw new DisabledException("User disabled");
        }

        String jwt = jwtUtils.createAccessToken(userDetails.getUser());

        userDetails.getUser().setPassword(null);

        HttpCookie cookie = cookieUtils.createJwtCookie(jwt);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(responseHeaders).body(userDetails.getUser());
    }

    @PostMapping("/test-no-auth")
    public String testNoAuth() {
        return "OK no auth";
    }

    @GetMapping("/test-no-auth")
    public String testNoAuthGet() {
        return "OK no auth";
    }

    @PostMapping("/test-with-auth")
    @PreAuthorize("USER")
    public String testWithAuth() {
        return "OK with auth";
    }
}

package ru.project.tasklist.auth.config.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.project.tasklist.auth.config.entity.User;
import ru.project.tasklist.auth.config.exception.UserOrEmailExistsException;
import ru.project.tasklist.auth.config.objects.JsonException;
import ru.project.tasklist.auth.config.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
@Log
public class AuthController {

    private UserService userService;
    public PasswordEncoder encoder;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @PutMapping("/register")
    public ResponseEntity register(@Valid @RequestBody User user) {

        if (userService.userExists(user.getUsername(), user.getEmail())) {
            throw new UserOrEmailExistsException("User or email already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        userService.save(user);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler
    public ResponseEntity<JsonException> handleExceptions(Exception ex) {
        return new ResponseEntity(new JsonException(ex.getClass().getSimpleName()), HttpStatus.BAD_REQUEST);
    }
}

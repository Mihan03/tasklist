package ru.project.tasklist.auth.config.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.project.tasklist.auth.config.entity.Activity;
import ru.project.tasklist.auth.config.entity.Role;
import ru.project.tasklist.auth.config.entity.User;
import ru.project.tasklist.auth.config.exception.RoleNotFoundException;
import ru.project.tasklist.auth.config.exception.UserAlreadyActivatedException;
import ru.project.tasklist.auth.config.exception.UserOrEmailExistsException;
import ru.project.tasklist.auth.config.objects.JsonException;
import ru.project.tasklist.auth.config.service.UserService;

import javax.validation.Valid;
import java.util.UUID;

import static ru.project.tasklist.auth.config.service.UserService.DEFAULT_ROLE;

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
        //uuid = "499efc70-0369-4663-9da0-8359ab06c8cb";
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
}

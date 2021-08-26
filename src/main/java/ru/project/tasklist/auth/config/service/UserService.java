package ru.project.tasklist.auth.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.tasklist.auth.config.entity.User;
import ru.project.tasklist.auth.config.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(long id) {
        return userRepository.findById(id).get();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean userExists(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            return true;
        }

        if (userRepository.existsByEmail(email)) {
            return true;
        }

        return false;
    }
}

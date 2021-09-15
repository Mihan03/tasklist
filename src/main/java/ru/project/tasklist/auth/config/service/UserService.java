package ru.project.tasklist.auth.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.tasklist.auth.config.entity.Activity;
import ru.project.tasklist.auth.config.entity.Role;
import ru.project.tasklist.auth.config.entity.User;
import ru.project.tasklist.auth.config.repository.ActivityRepository;
import ru.project.tasklist.auth.config.repository.RoleRepository;
import ru.project.tasklist.auth.config.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional

@Service
public class UserService {
    public static final String DEFAULT_ROLE = "USER";

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ActivityRepository activityRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, ActivityRepository activityRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.activityRepository = activityRepository;
    }

    public User getUser(long id) {
        return userRepository.findById(id).get();
    }

    public void register(User user, Activity activity) {
        userRepository.save(user);
        activityRepository.save(activity);
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

    public Optional<Role> findByName(String role) {
        return roleRepository.findByName(role);
    }

    public Activity saveActivity(Activity activity){
        return activityRepository.save(activity);
    }

    public Optional<Activity> findActivityByUiid(String uuid) {
        return activityRepository.findByUuid(uuid);
    }

    public int activate(String uuid) {
        return activityRepository.changeActivated(uuid, true);
    }

    public int deactivate(String uuid) {
        return activityRepository.changeActivated(uuid, false);
    }
}

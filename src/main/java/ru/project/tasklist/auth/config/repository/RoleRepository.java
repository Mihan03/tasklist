package ru.project.tasklist.auth.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.tasklist.auth.config.entity.Role;
import ru.project.tasklist.business.entity.Category;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}

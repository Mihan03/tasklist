package ru.project.tasklist.auth.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.tasklist.auth.config.entity.Activity;
import ru.project.tasklist.business.entity.Category;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

}

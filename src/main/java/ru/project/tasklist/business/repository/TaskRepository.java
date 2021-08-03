package ru.project.tasklist.business.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.project.tasklist.business.entity.Stat;
import ru.project.tasklist.business.entity.Task;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByUserEmailOrderByTitleAsc(String email);
}

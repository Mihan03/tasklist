package ru.project.tasklist.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import ru.project.tasklist.business.entity.Category;
import ru.project.tasklist.business.entity.Task;
import ru.project.tasklist.business.repository.CategoryRepository;
import ru.project.tasklist.business.repository.TaskRepository;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private TaskRepository repository;

    @Autowired
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findAll(String email) {
        return repository.findByUserEmailOrderByTitleAsc(email);
    }

    public Task add(Task task) {
        return repository.save(task);
    }

    public Task update(Task task) {
        return repository.save(task);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Task findById(Long id) {
        return repository.findById(id).get();
    }

    public Page<Task> find(String title, Integer completed, Long priorityId, Long categoryId, String email, Date dateFrom,
                           Date dateTo, PageRequest paging) {
        return repository.find(title, completed, priorityId, categoryId, email, dateFrom, dateTo, paging);
    };
}

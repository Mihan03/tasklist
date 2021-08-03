package ru.project.tasklist.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.tasklist.business.entity.Task;
import ru.project.tasklist.business.service.TaskService;
import ru.project.tasklist.business.util.MyLogger;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/task")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/all")
    public List<Task> findAll(@RequestBody String email) {

        MyLogger.debugMethodName("TaskController: findAll(email) --------------------------------------");

        return taskService.findAll(email);
    }

    @PutMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {
        MyLogger.debugMethodName("TaskController: add() --------------------------------------");

        if (task.getId() != null && task.getId() != 0) {
            return new ResponseEntity("redundant param: id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(taskService.add(task));
    }

    @PatchMapping("/update")
    public ResponseEntity<Task> update(@RequestBody Task task) {
        MyLogger.debugMethodName("TaskController: update() --------------------------------------");

        if (task.getId() == null && task.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        taskService.update(task);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody Long id) {

        MyLogger.debugMethodName("TaskController: delete() --------------------------------------");

        if (id == null && id == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            taskService.delete(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/id")
    public ResponseEntity findById(@RequestBody Long id) {
        MyLogger.debugMethodName("TaskController: findById() --------------------------------------");

        Task task = null;

        try {
            task = taskService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(task);
    }
}

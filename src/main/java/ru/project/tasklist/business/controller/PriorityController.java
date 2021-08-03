package ru.project.tasklist.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.tasklist.business.entity.Category;
import ru.project.tasklist.business.entity.Priority;
import ru.project.tasklist.business.search.CategorySearchValues;
import ru.project.tasklist.business.search.PrioritySearchValues;
import ru.project.tasklist.business.service.CategoryService;
import ru.project.tasklist.business.service.PriorityService;
import ru.project.tasklist.business.util.MyLogger;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private PriorityService priorityService;

    @Autowired
    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @PostMapping("/all")
    public List<Priority> findAll(@RequestBody String email) {

        MyLogger.debugMethodName("PriorityController: findAll(email) --------------------------------------");

        return priorityService.findAll(email);
    }

    @PutMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {
        MyLogger.debugMethodName("PriorityController: add() --------------------------------------");

        if (priority.getId() != null && priority.getId() != 0) {
            return new ResponseEntity("redundant param: id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(priorityService.add(priority));
    }

    @PatchMapping("/update")
    public ResponseEntity<Priority> update(@RequestBody Priority priority) {
        MyLogger.debugMethodName("PriorityController: update() --------------------------------------");

        if (priority.getId() == null && priority.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

        priorityService.update(priority);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody Long id) {

        MyLogger.debugMethodName("PriorityController: delete() --------------------------------------");

        if (id == null && id == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            priorityService.delete(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues) {
        MyLogger.debugMethodName("PriorityController: search() --------------------------------------");

        List<Priority> list = priorityService.find(prioritySearchValues.getTitle(), prioritySearchValues.getEmail());

        return ResponseEntity.ok(list);

    }

}

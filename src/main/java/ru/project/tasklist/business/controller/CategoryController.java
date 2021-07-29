package ru.project.tasklist.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.tasklist.business.entity.Category;
import ru.project.tasklist.business.service.CategoryService;
import ru.project.tasklist.business.util.MyLogger;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/all")
    public List<Category> findAll(@RequestBody String email) {

        MyLogger.debugMethodName("CategoryController: findAll(email) --------------------------------------");

        return categoryService.findAll("mihan0@mail.ru");
    }

    @PutMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {
        MyLogger.debugMethodName("CategoryController: add() --------------------------------------");

        if (category.getId() != null && category.getId() != 0) {
            return new ResponseEntity("redundant param: id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(categoryService.add(category));
    }
}

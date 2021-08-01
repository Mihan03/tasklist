package ru.project.tasklist.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.tasklist.business.entity.Category;
import ru.project.tasklist.business.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll(String email) {
        return categoryRepository.findByUserEmailOrderByTitleAsc(email);
    }

    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Category category) {
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> findByTitle(String text, String email) {
        return categoryRepository.findByTitle(text, email);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }
}

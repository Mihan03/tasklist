package ru.project.tasklist.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.tasklist.business.entity.Category;
import ru.project.tasklist.business.entity.Stat;
import ru.project.tasklist.business.repository.CategoryRepository;
import ru.project.tasklist.business.repository.StatRepository;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Transactional
public class StatService {

    private final StatRepository repository;

    @Autowired
    public StatService(StatRepository repository) {
        this.repository = repository;
    }

    public Stat findStat(String email) {
        return repository.findByUserEmail(email);
    }

}

package ru.project.tasklist.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.tasklist.business.entity.Priority;
import ru.project.tasklist.business.repository.PriorityRepository;

import java.util.List;

@Service
public class PriorityService {

    private PriorityRepository priorityRepository;

    @Autowired
    public PriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    public List<Priority> findAll(String email) {
        return priorityRepository.findByUserEmailOrderByIdAsc(email);
    }

    public Priority add(Priority priority) {
        return priorityRepository.save(priority);
    }

    public Priority update(Priority priority) {
        return priorityRepository.save(priority);
    }

    public void delete(Long id) {
        priorityRepository.deleteById(id);
    }

    public List<Priority> find(String text, String email) {
        return priorityRepository.find(text, email);
    }

    public Priority findById(Long id) {
        return priorityRepository.findById(id).get();
    }
}

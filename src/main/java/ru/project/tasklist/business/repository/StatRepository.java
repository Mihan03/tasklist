package ru.project.tasklist.business.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.project.tasklist.business.entity.Stat;

import java.util.List;

@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {
    Stat findByUserEmail(String email);
}

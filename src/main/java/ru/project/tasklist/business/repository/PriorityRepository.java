package ru.project.tasklist.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.tasklist.business.entity.Category;
import ru.project.tasklist.business.entity.Priority;

import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
    @Query("SELECT c FROM Priority c WHERE " +
            "(:title is null or :title='' " +
            "or lower(c.title) like lower(concat('%', :title, '%'))) " +

            " and c.user.email=:email " +

            "ORDER by c.title asc")

    List<Priority> find(@Param("title") String title, @Param("email") String email);

    List<Priority> findByUserEmailOrderByIdAsc(String email);
}

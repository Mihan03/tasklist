package ru.project.tasklist.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.tasklist.business.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserEmailOrderByTitleAsc(String email);

    @Query("SELECT c FROM Category c WHERE " +
    "(:title is null or :title='' " +
    "or lower(c.title) like lower(concat('%', :title, '%'))) " +

    " and c.user.email=:email " +

    "ORDER by c.title asc")

    List<Category> findByTitle(@Param("title") String title, @Param("email") String email);
}

package ru.project.tasklist.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.project.tasklist.auth.entity.Activity;

import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Activity a SET a.activated = :active WHERE a.uuid=:uuid")
    int changeActivated(@Param("uuid") String uuid, @Param("active") boolean active);

    Optional<Activity> findByUserId(long id);

    Optional<Activity> findByUuid(String uuid);


}

package ru.project.tasklist.business.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.project.tasklist.auth.config.entity.User;

import javax.persistence.*;
import java.util.Collection;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Setter
@Getter
public class Category {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String title;

    @Column(name = "completed_count", updatable = false)
    private Long completedCount;

    @Column(name = "uncompleted_count", updatable = false)
       private Long uncompletedCount;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
  }

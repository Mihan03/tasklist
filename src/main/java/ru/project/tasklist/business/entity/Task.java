package ru.project.tasklist.business.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.project.tasklist.auth.config.entity.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Setter
@Getter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private Short completed;

    @Column(name = "task_date")
    private Timestamp taskDate;

    @ManyToOne
    @JoinColumn(name = "priority_id", referencedColumnName = "id")
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

}

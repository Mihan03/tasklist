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
public class Priority {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String title;

    @Column
    private String color;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
 }

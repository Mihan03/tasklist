package ru.project.tasklist.business.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.project.tasklist.auth.config.entity.User;

import javax.persistence.*;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Setter
@Getter
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "completed_total", updatable = false)
    private Long completedTotal;


    @Column(name = "uncompleted_total", updatable = false)
    private Long uncompletedTotal;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
 }

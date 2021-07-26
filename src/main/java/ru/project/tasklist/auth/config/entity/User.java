package ru.project.tasklist.auth.config.entity;

import lombok.Getter;
import lombok.Setter;
import ru.project.tasklist.business.entity.Category;
import ru.project.tasklist.business.entity.Priority;
import ru.project.tasklist.business.entity.Stat;
import ru.project.tasklist.business.entity.Task;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USER_DATA")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    public Activity activity;

    @Email
    @Column
    private String email;

    @Column
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}

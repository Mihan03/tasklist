package ru.project.tasklist.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Setter
@DynamicUpdate
@Getter
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column
    private boolean activated;

    @NotBlank
    @Column(updatable = false)
    private String uuid;

    @JsonIgnore //чтобы не было бесконечного зацикливания (JSON не сможет сформироваться) - ссылку на Activity для JSON имеет только из User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
  }

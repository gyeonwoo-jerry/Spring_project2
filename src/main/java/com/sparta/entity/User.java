package com.sparta.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
@NoArgsConstructor

public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column
    private String email;
    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<UserSchedule> userScheduleList = new ArrayList<>();
}

package com.sparta.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "schedule") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Schedule extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String subject;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSchedule> userScheduleList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    public Schedule(String subject, String contents, User creator) {
        this.subject = subject;
        this.contents = contents;
        this.creator = creator;
    }

    public void update(User user, String subject, String contents) {
        this.contents = contents;
        this.subject = subject;
    }
}
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
    @Column(name = "schedule_id", nullable = false)
    private Long id;
    @Column
    private String subject;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<UserSchedule> userScheduleList = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Schedule(String subject, String contents, User user) {
        this.subject = subject;
        this.contents = contents;
        this.user = user;
    }

    public void update(String subject, String contents) {
        this.contents = contents;
        this.subject = subject;
    }
}
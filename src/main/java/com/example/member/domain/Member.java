package com.example.member.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberName;
    private Long age;
    private String email;
    private String phoneNumber;
    private Boolean isAdult;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 예: 가입 유형, 상태 등
    private String status;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getter/Setter 생략
    // ...
}


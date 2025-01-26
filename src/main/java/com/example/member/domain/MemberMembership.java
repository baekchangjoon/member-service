package com.example.member.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "member_memberships")
@Getter
@Setter
public class MemberMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private String membershipLevel; // SILVER, GOLD, PLATINUM
    private int points;

    // Getter/Setter ...
}


package com.example.member.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "member_addresses")
@Getter
@Setter
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String addressType;  // 예: 'HOME', 'OFFICE' 등
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String zipcode;

    // Getter/Setter ...
}


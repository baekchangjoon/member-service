package com.example.member.controller;

import com.example.member.MemberServiceApplication;
import com.example.member.domain.Member;
import com.example.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MemberServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    void testCreateAndGetMember() {
        Member member = new Member();
        member.setMemberName("홍길동");
        member.setEmail("hong@test.com");

        ResponseEntity<Member> createResp = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/members",
                member,
                Member.class
        );

        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Member created = createResp.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getMemberName()).isEqualTo("홍길동");

        ResponseEntity<Member> getResp = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/members/" + created.getId(),
                Member.class
        );
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResp.getBody().getEmail()).isEqualTo("hong@test.com");
    }
}


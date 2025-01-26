package com.example.member.service;

import com.example.member.domain.Member;
import com.example.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private MemberIntegrationService integrationService;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMember() {
        Member member = new Member();
        member.setMemberName("홍길동");
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Member created = memberService.createMember(member);
        assertNotNull(created);
        verify(kafkaTemplate, times(1)).send(eq("member-created-topic"), any(Member.class));
        verify(integrationService, times(1)).createDefaultLineForMember(member);
    }

    @Test
    void testGetMember() {
        Member member = new Member();
        member.setId(1L);
        member.setMemberName("테스터");
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Member found = memberService.getMember(1L);
        assertEquals("테스터", found.getMemberName());
    }
}

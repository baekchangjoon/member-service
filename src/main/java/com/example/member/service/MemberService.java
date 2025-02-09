package com.example.member.service;

import com.example.member.domain.Member;
import com.example.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MemberIntegrationService integrationService;

    public Member createMember(Member member) {
        Member saved = memberRepository.save(member);
        // 다른 서비스(Line-Service) 연동 (REST)
        integrationService.createDefaultLineForMember(saved);
        // Kafka 이벤트 발행 (Saga)
        kafkaTemplate.send("member-created-topic", saved);
        return saved;
    }

    public Member createMemberWithAge(Member member) {
        if (member.getAge() > 15)
            member.setIsAdult(true);
        else
            member.setIsAdult(false);

        Member saved = memberRepository.save(member);
        return saved;
    }

    @Cacheable(value = "memberCache", key = "#id")
    public Member getMember(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member updateMember(Long id, Member updated) {
        Member existing = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        existing.setMemberName(updated.getMemberName());
        existing.setEmail(updated.getEmail());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setStatus(updated.getStatus());
        // ...
        return memberRepository.save(existing);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
        // Kafka 이벤트 발행 가능
        kafkaTemplate.send("member-deleted-topic", id);
    }

    // 추가 API(멤버 상태 변경, 포인트 적립/차감, 멤버 등급 변경 등)들도 같은 패턴
}


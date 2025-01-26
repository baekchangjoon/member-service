package com.example.member.controller;

import com.example.member.domain.MemberMembership;
import com.example.member.repository.MemberMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members/{memberId}/membership")
public class MemberMembershipController {

    @Autowired
    private MemberMembershipRepository membershipRepository;

    @GetMapping
    public List<MemberMembership> getMembershipList(@PathVariable Long memberId) {
        // 실제론 memberId 필터링
        return membershipRepository.findAll();
    }

    @PostMapping
    public MemberMembership createMembership(@PathVariable Long memberId,
                                             @RequestBody MemberMembership membership) {
        membership.setMemberId(memberId);
        return membershipRepository.save(membership);
    }

    @PutMapping("/{membershipId}/points")
    public MemberMembership updatePoints(@PathVariable Long memberId,
                                         @PathVariable Long membershipId,
                                         @RequestParam int points) {
        MemberMembership mm = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Not found"));
        // 실제론 memberId 검증
        mm.setPoints(points);
        return membershipRepository.save(mm);
    }
}


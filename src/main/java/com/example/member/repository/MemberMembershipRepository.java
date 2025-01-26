package com.example.member.repository;

import com.example.member.domain.MemberMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberMembershipRepository extends JpaRepository<MemberMembership, Long> {
}

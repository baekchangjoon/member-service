package com.example.member.controller;

import com.example.member.domain.MemberAddress;
import com.example.member.repository.MemberAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members/{memberId}/addresses")
public class MemberAddressController {

    @Autowired
    private MemberAddressRepository addressRepository;

    @GetMapping
    public List<MemberAddress> getAllAddresses(@PathVariable Long memberId) {
        return addressRepository.findAll(); // 실제론 memberId로 필터링
    }

    @PostMapping
    public MemberAddress createAddress(@PathVariable Long memberId, @RequestBody MemberAddress addr) {
        addr.setMemberId(memberId);
        return addressRepository.save(addr);
    }

    @PutMapping("/{addressId}")
    public MemberAddress updateAddress(@PathVariable Long memberId,
                                       @PathVariable Long addressId,
                                       @RequestBody MemberAddress addr) {
        // 실제론 memberId & addressId 검증
        addr.setId(addressId);
        addr.setMemberId(memberId);
        return addressRepository.save(addr);
    }

    @DeleteMapping("/{addressId}")
    public void deleteAddress(@PathVariable Long memberId, @PathVariable Long addressId) {
        addressRepository.deleteById(addressId);
    }
}


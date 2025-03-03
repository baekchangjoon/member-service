package com.example.member.controller;

import com.example.member.domain.MemberAddress;
import com.example.member.dto.AddressRequest;
import com.example.member.dto.AddressResponse;
import com.example.member.repository.MemberAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.Socket;
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

//    @PostMapping
//    public MemberAddress createAddress(@PathVariable Long memberId, @RequestBody MemberAddress addr) {
//        addr.setMemberId(memberId);
//        return addressRepository.save(addr);
//    }
    @PostMapping
    public ResponseEntity<AddressResponse> validateAddress(
        @PathVariable("memberId") Long memberId,
        @RequestBody AddressRequest request) {

    // 소켓을 통해 주소판정서버에 주소 전송 및 결과 수신
    String validationResult = sendAddressToValidationServer(request.getAddress());
    return ResponseEntity.ok(new AddressResponse(validationResult));
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

    // 주소판정서버로 소켓 통신을 수행하는 메서드
    private String sendAddressToValidationServer(String address) {
        String host = "localhost"; // 주소판정서버가 같은 머신에 있는 경우
//        int port = 9000;
        int port = 9999;
        try (Socket socket = new Socket(host, port);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // 주소 데이터를 전송 (끝에 개행 문자 추가)
            writer.write(address);
            writer.newLine();
            writer.flush();

            // 서버로부터 응답(한 줄) 읽기
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "오류 발생";
        }
    }
}


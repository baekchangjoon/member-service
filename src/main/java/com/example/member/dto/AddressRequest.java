package com.example.member.dto;

// 요청 시 전달될 주소 정보
public class AddressRequest {
    private String address;

    public AddressRequest() {}

    public AddressRequest(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

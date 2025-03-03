package com.example.member.dto;

// 응답으로 전달될 판정 결과
public class AddressResponse {
    private String result;

    public AddressResponse() {}

    public AddressResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

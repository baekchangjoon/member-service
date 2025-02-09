package com.example.member.service;

import com.example.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MemberIntegrationService {

    // 간단 예시: Line-Service의 특정 엔드포인트를 직접 호출
    public void createDefaultLineForMember(Member member) {
        String lineServiceUrl = "http://localhost:8082/api/lines";

        CreateLineRequest request = new CreateLineRequest();
        request.setMemberId(member.getId());
        request.setLineNumber("010-0000-" + member.getId());
        request.setPlanName("DEFAULT_PLAN");

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(lineServiceUrl, request, Void.class);
    }

    static class CreateLineRequest {
        private Long memberId;
        private String lineNumber;
        private String planName;

        // getters/setters
        public Long getMemberId() {
            return memberId;
        }
        public void setMemberId(Long memberId) {
            this.memberId = memberId;
        }
        public String getLineNumber() {
            return lineNumber;
        }
        public void setLineNumber(String lineNumber) {
            this.lineNumber = lineNumber;
        }
        public String getPlanName() {
            return planName;
        }
        public void setPlanName(String planName) {
            this.planName = planName;
        }
    }
}

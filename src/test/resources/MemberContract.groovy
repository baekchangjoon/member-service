org.springframework.cloud.contract.spec.Contract.make {
    description "회원 조회 테스트"
    request {
        method 'GET'
        url '/api/members/1'
    }
    response {
        status 200
        body(
                id: 1,
                memberName: "홍길동",
                email: "hong@test.com",
                phoneNumber: $(consumer(regex('.+')), producer('010-0000-0000')),
                status: "ACTIVE"
        )
        headers {
            contentType(applicationJson())
        }
    }
}

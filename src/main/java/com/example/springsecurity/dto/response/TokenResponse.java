package com.example.springsecurity.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    //key truy cap vao he thong
    private String accessToken;

    //lam moi token
    private String refreshToken;

    private Long userId;

    //co the tra ve quoc gia,
}

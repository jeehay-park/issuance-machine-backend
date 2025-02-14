package com.ictk.issuance.data.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserLoginRequest {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserLoginRequestRQB {

        @Schema(description = "패스워드 해시")
        private String passwordHash;

        @Schema(description = "사용자 id")
        private String userId;

        @Schema(description = "uuid")
        private String uuid;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data

    public static class UserLoginRequestRSB {

        @Schema(description = "세션 id")
        private String sessionId;

        @Schema(description = "엑세스 토큰 정보")
        private Token token;

        @Schema(description = "세션키")
        private String sessionKey;

        @Schema(description = "로그인 사용자 등록 정보")
        private UserInfo userInfo;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Token {

        @Schema(description = "엑세스 토큰")
        private String accessToken;

        @Schema(description = "토큰 만료 시각")
        private int expiry;

        @Schema(description = "토큰 허용 시간")
        private long expirationTime;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UserInfo {
        @Schema(description = "사용자 id")
        private String userId;

        @Schema(description = "사용자 이름")
        private String name;

        @Schema(description = "사용자 email")
        private String email;
    }
}

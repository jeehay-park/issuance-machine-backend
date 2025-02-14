package com.ictk.issuance.data.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserSignUpDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserSignUpRQB {

        @NotNull
        @Schema(description = "사용자 ID")
        private String userId;

        @NotNull
        @Schema(description = "사용자 비밀번호")
        private String password;

        @NotNull
        @Schema(description = "사용자 이름")
        private String name;

        @NotNull
        @Schema(description = "사용자 이메일")
        private String email;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class UserSignUpRSB {

        @NotNull
        @Schema(description = "사용자 ID")
        private String userId;

    }
}

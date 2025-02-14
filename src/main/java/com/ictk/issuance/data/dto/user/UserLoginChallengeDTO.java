package com.ictk.issuance.data.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserLoginChallengeDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserLoginChallengeRQB {

        @NotNull
        @Schema(description = "사용자 id")
        private String userId;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class UserLoginChallengeRSB {

        @Schema(description = "pass_salt")
        private String salt;

        @Schema(description = "uuid")
        private String uuid;

    }
}

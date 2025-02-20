package com.ictk.issuance.data.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class UserEditDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserEditRQB {
        @NotNull
        @Schema(description = "사용자 Id")
        private String userId;

        private String name;

        private String email;

        private String password;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class UserEditRSB {
        @NotNull
        @Schema(description = "사용자 Id")
        private String userId;
    }
}

package com.ictk.issuance.data.dto.workinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ictk.issuance.common.annotations.ValidateString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WorkControlDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WorkControlRQB {
        @NotNull
        @Schema(description = "발급작업 ID")
        private String workId;

        @NotNull
        @Schema(description = "발급작업 제어 명령")
        @ValidateString(acceptedValues = {"START", "STOP", "RESTORE", "FINISH"})
        private String command;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class WorkControlRSB {
        @NotNull
        @Schema(description = "작업 제어(커맨드) 결과 SUCC / FAIL")
        private String commandResult;

        @Schema(description = "제어 결과 메시지")
        private String resultMessage;
    }
}

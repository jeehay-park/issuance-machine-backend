package com.ictk.issuance.data.dto.workinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public class WorkInfoDeleteDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WorkInfoDeleteRQB {

        @NotNull
        private String workId;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkInfoDeleteRSB {

        @NotNull
        @Schema(description = "작업 삭제결과 SUCC / FAIL")
        private String result;

        @Schema(description = "작업 삭제개수 (0 이상이면 삭제됨)")
        private int deleteCnt;
    }
}
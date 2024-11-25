package com.ictk.issuance.data.dto.programinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProgramInfoDeleteDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProgramInfoDeleteRQB {

        @NotNull(message = "workId는 필수 값입니다.")
        @Schema(description = "발급작업 ID")
        private String progId;


    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ProgramInfoDeleteRSB {

        @Schema(description = "프로그램 삭제 결과")
        private String result;

        @Schema(description = "프로그램 삭제 개수")
        private int deleteCnt;

    }


}

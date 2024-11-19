package com.ictk.issuance.data.dto.programinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProgramInfoSaveDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProgramInfoSaveRQB {

        @Schema(description = "프로그램 이름")
        private String progName;

        @Schema(description = "제품 (칩 Chip)")
        private String product;

        @Schema(description = "프로그램 상태")
        private String status;

        @Schema(description = "프로파일 ID. 'prof_' + seq의 형식")
        private String profId;


    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ProgramInfoSaveRSB {

        @Schema(description = "프로그램 생성/변경 결과 SUCC / FAIL")
        private String result;

        @Schema(description = "프로그램 생성/변경 건수")
        private int updateCnt;
    }

}

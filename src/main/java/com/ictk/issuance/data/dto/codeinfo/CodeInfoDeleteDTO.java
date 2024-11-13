package com.ictk.issuance.data.dto.codeinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CodeInfoDeleteDTO {
    // 코드 정보 삭제

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CodeInfoDeleteRQB {

        @Schema(description = "코드 정보 ID")
        @NotNull(message = "codeId는 필수 값입니다.")
        private String codeId;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CodeInfoDeleteRSB {

        @Schema(description = "코드 정보 삭제 결과")
        private String result;

        @Schema(description = "코드 정보 삭제 개수")
        private int deleteCnt;
    }


}

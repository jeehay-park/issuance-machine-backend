package com.ictk.issuance.data.dto.snrule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SNRuleDeleteDTO {

    /**
     * 시리얼넘버(SN) 규칙 삭제
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class SNRuleDeleteRQB {

        @Schema(description = "SN규칙 ID")
        @NotNull(message = "snrId는 필수 값입니다.")
        private String snrId;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SNRuleDeleteRSB {

        @Schema(description = "SN규칙 삭제결과")
        private String result;

        @Schema(description = "SN규칙 삭제개수")
        private int deleteCnt;

    }

}

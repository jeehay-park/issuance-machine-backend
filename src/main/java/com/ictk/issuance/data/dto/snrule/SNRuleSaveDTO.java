package com.ictk.issuance.data.dto.snrule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class SNRuleSaveDTO {
    /**
     * 시리얼넘버(SN) 규칙 생성/갱신
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class SNRuleSaveRQB {

        @Schema(description = "SN규칙 ID")
        private String snrId;

        @Schema(description = "SN규칙 이름")
        @NotNull(message = "snrName은 필수 값입니다.")
        private String snrName;

        @Schema(description = "테스트 코드")
        @NotNull(message = "testCode는 필수 값입니다.")
        private String testCode;

        @Schema(description = "로케이션")
        private int location;

        @Schema(description = "최근 Burn 시간")
        private String lastBurnDate;

        @Schema(description = "금일 건수")
        @NotNull(message = "todayCount는 필수 값입니다.")
        private long todayCount;

        @Schema(description = "건수 합계")
        @NotNull(message = "countSum은 필수 값입니다.")
        private long countSum;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SNRuleSaveRSB {

        @Schema(description = "SN규칙 ID")
        private String snrId;

        @Schema(description = "SN규칙 이름")
        private String snrName;

    }



}

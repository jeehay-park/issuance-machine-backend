package com.ictk.issuance.data.dto.snrule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SNRuleSearchDTO {

    /**
     * 시리얼넘버(SN) 규칙 정보 조회
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SNRuleSearchRQB {

        @Schema(description = "SN규칙 ID")
        @NotNull(message = "snrId는 필수 값입니다.")
        private String snrId;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SNRuleSearchRSB {

        @Schema(description = "SN규칙 ID")
        private String snrId;

        @Schema(description = "SN규칙 이름")
        private String snrName;

        @Schema(description = "테스트 코드")
        private String testCode;

        @Schema(description = "로케이션")
        private int location;

        @Schema(description = "최근 Burn 시간")
        private String lastBurnDate;

        @Schema(description = "금일 건수")
        private long todayCount;

        @Schema(description = "건수 합계")
        private long countSum;

        @Schema(description = "등록일자")
        private String createdAt;

    }


}

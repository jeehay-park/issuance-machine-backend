package com.ictk.issuance.data.dto.workinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public class WorkInfoSaveDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WorkInfoSaveRQB {

        @Schema(description = "발급작업 ID. 'wrk_' + seq의 형식. (작업 변경시 필수)")
        private String workId;

        @NotNull
        @Schema(description = "발급작업 표시 넘버")
        private String workNo;

        @NotNull
        @Schema(description = "발급작업 태그 명")
        private String tagName;

        @NotNull
        @Schema(description = "고객사")
        private String customer;

        @NotNull
        @Schema(description = "오더 넘버 (발주번호)")
        private String orderNo;

        @NotNull
        @Schema(description = "디바이스 이름")
        private String deviceName;

        @NotNull
        @Schema(description = "프로그램 ID. 'prog_' + seq의 형식")
        private String progId;

        @NotNull
        @Schema(description = "발급머신 ID. 'mcn_' + seq의 형식")
        private String mcnId;

        @NotNull
        @Schema(description = "SN규칙 ID. 'snr_' + seq의 형식")
        private String snrId;

        @NotNull
        @Schema(description = "발급칩의 LOCK 여부. true/false")
        private boolean isLock;

        @Schema(description = "목표수량")
        private long targetQnty;

        @Schema(description = "작업완료 예정시간. yyyy-MM-dd HH:mm:ss 포맷")
        protected String dueDate;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class WorkInfoSaveRSB {

        @NotNull
        @Schema(description = "발급작업 ID. 'wrk_' + seq의 형식")
        private String workId;

        @Schema(description = "발급작업 표시 넘버")
        private String workNo;

    }
}
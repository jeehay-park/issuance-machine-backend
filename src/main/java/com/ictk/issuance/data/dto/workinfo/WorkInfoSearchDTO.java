package com.ictk.issuance.data.dto.workinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ictk.issuance.common.annotations.ValidateString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WorkInfoSearchDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WorkInfoSearchRQB {

        @NotNull
        @Schema(description = "발급작업 ID. 'wrk_' + seq의 형식")
        private String workId;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class WorkInfoSearchRSB {

        @NotNull
        @Schema(description = "발급작업 ID. 'wrk_' + seq의 형식")
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
        @Schema(description = "프로그램 이름")
        private String progName;

        @NotNull
        @Schema(description = "발급기 고유 ID. 'mcn_' + seq의 형식")
        private String mcnId;

        @NotNull
        @Schema(description = "발급기 이름")
        private String mcnName;

        @NotNull
        @Schema(description = "SN규칙 ID. 'snr_' + seq의 형식")
        private String snrId;

        @NotNull
        @Schema(description = "SN 규칙 대상 제품 이름 ")
        private String snrName;

        @Schema(description = "발급칩의 LOCK 여부. true/false", nullable = false)
        private Boolean isLock;

        @Schema(description = "목표수량")
        private int targetSize;

        @Schema(description = "완성수량")
        private int completedSize;

        @Schema(description = "잔여수량")
        private int remainedSize;

        @Schema(description = "실패수량")
        private int failedSize;

        @NotNull
        @ValidateString(acceptedValues = {"INIT", "READY", "RUNNING", "ON_STOP", "FINISHED"})
        @Schema(description = "작업 상태 INIT/READY/RUNNING/ON_STOP/FINISHED")
        private String status;

        @NotNull
        @Schema(description = "작업 등록시간. yyyy-MM-dd HH:mm:ss 포맷")
        private String createdAt;

        @Schema(description = "작업완료 예정시간. yyyy-MM-dd HH:mm:ss 포맷")
        private String dueDate;

    }
}

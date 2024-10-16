package com.ictk.issuance.data.dto.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ictk.issuance.common.annotations.ValidateString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ConfigDeleteDTO {

    /**
     * 발급설정 (프로파일/키발급코드/스크립트) 삭제
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class ConfigDeleteRQB {

        @Schema(description = "발급설정 대상 타입")
        @ValidateString(acceptedValues={"PROFILE","KEYISSUE","SCRIPT"}, message="configType이 유효하지 않습니다.")
        @NotNull(message = "configType은 필수 값입니다.")
        private String configType;

        private String profId;

        private String keyisId;

        private String scrtId;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfigDeleteRSB {

        @Schema(description = "발급설정 삭제결과")
        private String result;

        @Schema(description = "발급설정 삭제개수")
        private int deleteCnt;

    }

}

package com.ictk.issuance.data.dto.config;

import com.ictk.issuance.data.dto.config.ConfigDTO.ProfileConfigObj;
import com.ictk.issuance.data.dto.config.ConfigDTO.KeyissueConfigObj;
import com.ictk.issuance.data.dto.config.ConfigDTO.ScriptConfigObj;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ictk.issuance.common.annotations.ValidateString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ConfigSearchDTO {

    /**
     * 발급설정(프로파일/키발급코드/스크립트) (단일) 조회
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class ConfigSearchRQB {

        @Schema(description = "발급설정 대상 타입")
        @ValidateString(acceptedValues={"PROFILE","KEYISSUE","SCRIPT"}, message="configType이 유효하지 않습니다.")
        @NotNull(message = "configType은 필수 값입니다.")
        private String configType;

        @Schema(description = "프로파일 ID")
        private String profId;

        @Schema(description = "키발급코드 ID")
        private String keyisId;

        @Schema(description = "스크립트 ID")
        private String scrtId;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfigSearchRSB {

        @Schema(description = "발급설정 대상 타입")
        private String configType;

        private ProfileConfigObj profileConfig;

        private KeyissueConfigObj keyissueConfig;

        private ScriptConfigObj scriptConfig;

    }
}

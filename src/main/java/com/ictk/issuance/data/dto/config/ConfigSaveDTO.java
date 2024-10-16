package com.ictk.issuance.data.dto.config;

import com.ictk.issuance.data.dto.config.ConfigDTO.ProfileConfigBaseObj;
import com.ictk.issuance.data.dto.config.ConfigDTO.KeyissueConfigBaseObj;
import com.ictk.issuance.data.dto.config.ConfigDTO.ScriptConfigBaseObj;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ictk.issuance.common.annotations.ValidateString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ConfigSaveDTO {

    /**
     * 발급설정(프로파일/키발급코드/스크립트) 추가(생성)/변경(편집)
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class ConfigSaveRQB {

        @Schema(description = "발급설정 대상 타입")
        @ValidateString(acceptedValues={"PROFILE","KEYISSUE","SCRIPT"}, message="configType이 유효하지 않습니다.")
        @NotNull(message = "configType은 필수 값입니다.")
        private String configType;

        private ProfileConfigBaseObj profileConfig;

        private KeyissueConfigBaseObj keyissueConfig;

        private ScriptConfigBaseObj scriptConfig;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfigSaveRSB {

        private String profId;

        private String profName;

        private String keyisId;

        private String keyisName;

        private String scriptId;

        private String scriptName;

    }

}

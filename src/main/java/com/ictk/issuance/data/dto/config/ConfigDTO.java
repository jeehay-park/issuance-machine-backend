package com.ictk.issuance.data.dto.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class ConfigDTO {

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileConfigBaseObj {

        @Schema(description = "프로파일 ID")
        private String profId;

        @Schema(description = "프로파일 이름")
        private String profName;

        @Schema(description = "상세설명")
        private String description;

        @Schema(description = "프로파일 타입")
        private String profType;

        @Schema(description = "버전")
        private String version;

        @Schema(description = "프로파일 데이터")
        private String ctntData;

        @Schema(description = "데이터 해시")
        private String dataHash;

    }

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileConfigObj extends ProfileConfigBaseObj {

        @Schema(description = "업데이트 시간")
        private String updatedAt;

        @Schema(description = "등록시간")
        private String createdAt;
    }

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeyissueConfigBaseObj {

        @Schema(description = "키발급코드 ID")
        private String keyisId;

        @Schema(description = "키발급코드 이름")
        private String keyisName;

        @Schema(description = "상세설명")
        private String description;

        @Schema(description = "키발급코드 타입")
        private String keyisType;

        @Schema(description = "버전")
        private String version;

        @Schema(description = "프로파일 데이터")
        private String ctntData;

        @Schema(description = "데이터 해시")
        private String dataHash;

    }

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeyissueConfigObj extends KeyissueConfigBaseObj {

        @Schema(description = "업데이트 시간")
        private String updatedAt;

        @Schema(description = "등록시간")
        private String createdAt;
    }

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScriptConfigBaseObj {

        @Schema(description = "스크립트 ID")
        private String scrtId;

        @Schema(description = "스크립트 이름")
        private String scrtName;

        @Schema(description = "상세설명")
        private String description;

        @Schema(description = "스크립트 타입")
        private String scrtType;

        @Schema(description = "버전")
        private String version;

        @Schema(description = "프로파일 데이터")
        private String ctntData;

        @Schema(description = "데이터 해시")
        private String dataHash;

    }

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScriptConfigObj extends ScriptConfigBaseObj {

        @Schema(description = "업데이트 시간")
        private String updatedAt;

        @Schema(description = "등록시간")
        private String createdAt;
    }

}

package com.ictk.issuance.data.dto.programinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ictk.issuance.data.dto.config.ConfigDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


// @NoArgsConstructor // Only if needed by frameworks
public class ProgramInfoSearchDTO {

    @Builder
    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProgramInfoSearchRQB {

        @Schema(description = "발급 프로그램 ID")
        @NotNull(message = "progId는 필수값입니다.")
        private String progId;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProgramInfoSearchRSB {

        @Schema(description = "발급기 ID")
        private String progId;

        @Schema(description = "발급작업 표시 넘버")
        private String progName;

        @Schema(description = "프로그램 상세설명")
        private String description;

        @Schema(description = "제품 (칩 Chip)")
        private String product;

        @Schema(description = "발급 핸들러 이름 (클래스 코드로 관리)")
        private String sessionHandler;

        @Schema(description = "제품 테스트 코드")
        private String testCode;

        @Schema(description = "etcOption")
        private String etcOption;

        @Schema(description = "프로파일 설정정보")
        private List<ConfigDTO.ProfileConfigObj> profileInfo;

        @Schema(description = "키발급코드 설정정보")
        private List<ConfigDTO.KeyissueConfigObj> keyIssueInfo;

        @Schema(description = "스크립트 설정정보")
        private  List<ConfigDTO.ScriptConfigObj> scriptInfo;

        @Schema(description = "SN 인크립션 여부")
        private boolean isEncryptSn;

        @Schema(description = "회사코드")
        private String companyCode;

        @Schema(description = "회사코드")
        private String countryCode;

        @Schema(description = "국가코드")
        private String interfaceType;

        @Schema(description = "패키지 타입")
        private String packageType;

        @Schema(description = "프로그램 등록시간")
        private String createdAt;

    }
//
//    // profileInfo
//    @Data
//    @SuperBuilder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class ProfileInfo {
//
//        @Schema(description = "프로파일 ID")
//        private String profId;
//
//        @Schema(description = "발급기 이름")
//        private String profName;
//
//        @Schema(description = "설명")
//        private String description;
//    }
//
//    // keyIssueInfo
//    @Data
//    @SuperBuilder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class KeyIssueInfo {
//
//        @Schema(description = "키발급코드 ID")
//        private String keyisId;
//
//        @Schema(description = "키발급코드 이름")
//        private String keyisName;
//
//        @Schema(description = "설명")
//        private String description;
//    }
//
//    // scriptInfo
//    @Data
//    @SuperBuilder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class ScriptInfo {
//
//        @Schema(description = "스크립트 ID")
//        private String scrtId;
//
//        @Schema(description = "스크립트 이름")
//        private String scrtName;
//
//        @Schema(description = "설명")
//        private String description;
//    }

}

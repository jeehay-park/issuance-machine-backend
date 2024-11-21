package com.ictk.issuance.data.dto.programinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class ProgramInfoSaveDTO {

    @Builder
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProgramInfoSaveRQB {

        @Schema(description = "발급 프로그램 고유 ID")
        private String progId;

        @NotNull(message = "progName은 필수 값입니다.")
        @Schema(description = "프로그램 이름")
        private String progName;

        @Schema(description = "프로그램 상세설명")
        private String description;

        @NotNull(message = "product은 필수 값입니다.")
        @Schema(description = "제품 (칩 Chip)")
        private String product;

        @NotNull(message = "sessionHandler은 필수 값입니다.")
        @Schema(description = "발급 핸들러 이름 (클래스 코드로 관리)")
        private String sessionHandler;

        @Schema(description = "제품 테스트 코드")
        private String testCode;

        @Schema(description = "기타코드 옵션. 여러값 가능(리스트)")
        private List<String> etcOption;

        @NotNull(message = "profId은 필수 값입니다.")
        @Schema(description = "프로파일 ID")
        private String profId;

        @Schema(description = "키발급코드 ID")
        private String keyisId;

        @Schema(description = "스크립트 ID")
        private String scrtId;

        @NotNull(message = "isEncryptSn은 필수 값입니다.")
        @Schema(description = "SN 인크립션 여부")
        private boolean isEncryptSn;

        @Schema(description = "회사코드")
        private String companyCode;

        @Schema(description = "국가코드")
        private String countryCode;

        @Schema(description = "인터페이스 타입")
        private String interfaceType;

        @Schema(description = "패키지 타입")
        private String packageType;

    }

    @Builder
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProgramInfoSaveRSB {

        @NotNull(message = "progId은 필수 값입니다.")
        @Schema(description = "발급 프로그램 고유 ID")
        private String  progId;

        @NotNull(message = "progName은 필수 값입니다.")
        @Schema(description = "발급작업 표시 넘버")
        private int progName;

    }

}

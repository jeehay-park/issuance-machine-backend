package com.ictk.issuance.data.dto.codeinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CodeInfoSaveDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CodeInfoSaveRQB{

        @Schema(description = "코드 ID")
        private String codeId;

        @Schema(description = "코드 이름")
        @NotNull(message = "코드 이름은 필수 값입니다.")
        private String codeName;

        @Schema(description = "코드 그룹")
        @NotNull(message = "코드 그룹은 필수 값입니다.")
        private String codeGroup;

        @Schema(description = "코드 상세 설명")
        private String description;

        @Schema(description = "코드 상태")
        private String status;



    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CodeInfoSaveRSB{

        @Schema(description = "코드 ID")
        private String codeId;

        @Schema(description = "코드 이름")
        private String codeName;
    }
}

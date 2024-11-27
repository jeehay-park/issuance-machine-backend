package com.ictk.issuance.data.dto.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ictk.issuance.common.annotations.ValidateString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ConfigIdListDTO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfigIdListRSB {

        @Schema(description = "프로파일 ID 목록")
        private List<String> profIdList;

        @Schema(description = "키발급코드 ID 목록")
        private List<String> keyisIdList;

        @Schema(description = "스크립트 ID 목록")
        private List<String> scrtIdList;

    }


}

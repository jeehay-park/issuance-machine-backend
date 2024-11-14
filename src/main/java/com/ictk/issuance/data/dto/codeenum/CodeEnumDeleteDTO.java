package com.ictk.issuance.data.dto.codeenum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;


public class CodeEnumDeleteDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
public static class CodeEnumDeleteRQB {

        @Schema(description = "코드 정보 ID")
        @NotNull(message = "codeId는 필수 값입니다.")
        private String codeId;

    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
public static class CodeEnumDeleteRSB {

    @Schema(description = "Code ENUM 삭제 결과")
    private String result;

    @Schema(description = "Code ENUM 삭제 개수")
    private int deleteCnt;
}


}

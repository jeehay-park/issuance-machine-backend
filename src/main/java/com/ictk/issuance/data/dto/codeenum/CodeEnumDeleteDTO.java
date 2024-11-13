package com.ictk.issuance.data.dto.codeenum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public class CodeEnumDeleteDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
public static class CodeEnumDeleteRQB {

    @Schema(description = "코드 ENUM Id")
    @NotNull(message = "codeId는 필수 값입니다.")

       private List<String>  codeEnumIds;
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

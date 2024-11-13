package com.ictk.issuance.data.dto.codeenum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class CodeEnumSaveDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data // @Data annotation from Lombok generates getter and setter methods for all fields in the class, along with other common methods like equals(), hashCode(), and toString(). This means that even though you haven't explicitly defined getDeviceList(), Lombok is automatically generating that method for you.
    @JsonIgnoreProperties(ignoreUnknown =true)
public static class CodeEnumSaveRQB {

    private String codeId;

    private List<CodeEnumDTO.CodeEnumObj> enumList;
}

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
public static class CodeEnumSaveRSB {
    @Schema(description = "코드 ENUM 생성/변경 결과 SUCC / FAIL")
    private String result;

    @Schema(description = "ENUM 생성/변경 건수")
    private int updateCnt;

}

}

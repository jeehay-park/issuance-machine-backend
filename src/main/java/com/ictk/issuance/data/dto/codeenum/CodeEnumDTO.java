package com.ictk.issuance.data.dto.codeenum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

public class CodeEnumDTO {

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
public static class  CodeEnumObj {

        @Schema(description = "코드 ENUM 값")
        private String enumValue;

        @Schema(description = "해당 ENUM의 필수유무. true / false")
        private Boolean isMandatory;

        @Schema(description = "디바이스 IP 주소")
        private String ip;

        @Schema(description = "코드 ENUM 표시순서")
        private int order;

        @Schema(description = "코드 ENUM에 대한 상세설명")
        private String description;

        // Constructor with the required fields
        public CodeEnumObj(String enumValue, Boolean isMandatory, String ip, String description) {
            this.enumValue = enumValue;
            this.isMandatory = isMandatory;
            this.ip = ip;
            this.description = description;
        }

}

}

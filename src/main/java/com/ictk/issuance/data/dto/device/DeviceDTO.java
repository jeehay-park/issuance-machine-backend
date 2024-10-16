package com.ictk.issuance.data.dto.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class DeviceDTO {


    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceBaseObj {

        @Schema(description = "디바이스 ID")
        private String dvcId;

        @Schema(description = "디바이스 이름")
        private String dvcName;

        @Schema(description = "디바이스 번호, 1부터")
        private int dvcNum;

        @Schema(description = "IP 주소")
        private String ip;

        @Schema(description = "롬(ROM) 버전")
        private String romVer;

    }

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceObj extends DeviceBaseObj{

        @Schema(description = "업데이트 시간")
        private String updatedAt;

        @Schema(description = "등록시간")
        private String createdAt;

    }

}

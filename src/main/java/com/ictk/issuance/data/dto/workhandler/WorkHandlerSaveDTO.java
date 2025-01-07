package com.ictk.issuance.data.dto.workhandler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WorkHandlerSaveDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
public static class WorkHandlerSaveRQB {

        @NotNull
        @Schema(description = "발급작업 ID. 'wrk_' + seq의 형식")
        private String workId;

        @NotNull
        @Schema(description = "발급기 디바이스 고유 ID. 'dvc_' + seq의 형식")
        private String dvcId;

        @Schema(description = "작업 핸들러 이름. (값을 입력하지 않으면 자동으로 생성)")
        private String hdlName;

    }
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
public static class WorkHandlerSaveRSB {

    private String hdlId;

    private String hdlName;

}

}

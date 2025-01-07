package com.ictk.issuance.data.dto.workhandler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

public class WorkHandlerListDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WorkHandlerListRQB {

        @NotNull(message = "workId는 필수 값입니다.")
        @Schema(description = "발급작업 ID. 'wrk_' + seq의 형식")
        private String workId;

        @Schema(description = "핸들러 이름 (like 쿼리)")
        private String hdlName;

        @Schema(description = "디바이스 이름 ( like 쿼리)")
        private String dvcName;

        @Schema(description = "정렬순서. ASC/DESC")
        private String order;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkHandlerListRSB {

        private List<Map<String, Object>> workHandlers;
    }


}

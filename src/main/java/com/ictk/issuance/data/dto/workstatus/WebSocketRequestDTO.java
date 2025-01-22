package com.ictk.issuance.data.dto.workstatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WebSocketRequestDTO {


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebSocketRequestHeader {

        @NotNull
        @Schema(description = "데이터 타입을 지정. reconnect, disconnect, data, release 등")
        private String type;

        @Schema(description = "웹소켓 연결(connected)시 발급한 클라이언트 고유 세션id. (UUID)")
        private String clientId;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class WebSocketRequestBody {

        @NotNull
        @Schema(description = "작업 ID")
        private String workId;

        @Schema(description = "대시보드 상세정보 수신 주기. 단위 (초)")
        private String duration;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class WebSocketRequest {

        @NotNull
        @Schema(description = "웹소켓 요청 헤더")
        private WebSocketRequestHeader header;

        @NotNull
        @Schema(description = "웹소켓 요청 본문")
        private WebSocketRequestBody body;
    }

}

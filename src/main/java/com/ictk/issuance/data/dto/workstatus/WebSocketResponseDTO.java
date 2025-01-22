package com.ictk.issuance.data.dto.workstatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class WebSocketResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebSocketResponseHeader {

        @Schema(description = "trId값")
        private String trId;

        @Schema(description = "데이터 타입을 지정. reconnect, disconnect, data, release 등")
        private String type;

        @NotNull
        @Schema(description = "데이터 응답코드. 00000 성공, 그 외의 경우는 실패")
        private String rtnCode;

        @Schema(description = "응답코드에 대응하는 응답메시지")
        private String rtnMessage;

    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebSocketResponseBody {
        @NotNull
        @Schema(description = "작업 ID")
        private String workId;

        @NotNull
        @Schema(description = "작업 시작 시간")
        private String startedAt;

        @NotNull
        @Schema(description = "작업 완료 예정 시간")
        private String completedExpAt;

        @NotNull
        @Schema(description = "남은 시간")
        private String remainedTime;

        @Schema(description = "자원 정보")
        private ResourceInfo resourceInfo;

        @NotNull
        @Schema(description = "목표 수량")
        private int targetQnty;

        @NotNull
        @Schema(description = "완료된 수량")
        private int completedQnty;

        @NotNull
        @Schema(description = "남은 수량")
        private int remainedQnty;

        @NotNull
        @Schema(description = "실패된 수량")
        private int failedQnty;

        @Schema(description = "샘플 수량")
        private int sampleQnty;

        @NotNull
        @Schema(description = "작업 상태")
        private String workStatus;

        @Schema(description = "디바이스 핸들러")
        private List<DeviceHandler> deviceHandlers;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceInfo {
        private String cpuUsage;
        private String memUsage;
        private String memIncrease;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceHandler {
        private String mcnName;
        private String dvcId;
        private String dvcName;
        private String dvcIp;
        private String hdlId;
        private String hdlName;
        private String hdlStatus;
        private String detailMsg;
        private String sessId;
        private String sessionNo;
        private String sessionDate;
        private String sessionStatus;
        private String tkMsecTime;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class WebSocketResponse {

        @NotNull
        @Schema(description = "웹소켓 응답 헤더")
        private WebSocketResponseHeader header;

        @NotNull
        @Schema(description = "웹소켓 응답 본문")
        private WebSocketResponseBody body;
    }
}

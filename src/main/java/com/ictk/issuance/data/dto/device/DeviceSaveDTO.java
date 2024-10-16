package com.ictk.issuance.data.dto.device;

import com.ictk.issuance.data.dto.device.DeviceDTO.DeviceBaseObj;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public class DeviceSaveDTO {

    /**
     * 발급기계 디바이스 생성/갱신
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class DeviceSaveRQB {

        @Schema(description = "발급기 ID")
        @NotNull(message = "mcnId는 필수 값입니다.")
        private String mcnId;

        @Schema(description = "발급기의 디바이스 목록")
        private List<DeviceBaseObj> deviceList;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceSaveRSB {

        @Schema(description = "디바이스 생성/갱신 결과")
        private String result;

        @Schema(description = "디바이스 생성/갱신 개수")
        private int updateCnt;

    }


}

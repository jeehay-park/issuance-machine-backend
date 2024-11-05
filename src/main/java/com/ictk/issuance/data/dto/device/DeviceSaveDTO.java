package com.ictk.issuance.data.dto.device;

import com.ictk.issuance.data.dto.device.DeviceDTO.DeviceBaseObj;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


public class DeviceSaveDTO {

    /**
     * 발급기계 디바이스 생성/갱신
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data // @Data annotation from Lombok generates getter and setter methods for all fields in the class, along with other common methods like equals(), hashCode(), and toString(). This means that even though you haven't explicitly defined getDeviceList(), Lombok is automatically generating that method for you.
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class DeviceSaveRQB {

        @Schema(description = "발급기 ID")
        @NotNull(message = "mcnId는 필수 값입니다.")
        @Getter
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

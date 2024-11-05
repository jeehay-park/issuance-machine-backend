package com.ictk.issuance.data.dto.machine;

import com.ictk.issuance.data.dto.device.DeviceDTO.DeviceObj;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class MachineSearchDTO {

    /**
     * 발급기계(단일) 조회
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class MachineSearchRQB {

        @Schema(description = "발급기 ID", defaultValue = "mcn0001")
        @NotNull(message = "mcnId는 필수 값입니다.")
        private String mcnId;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MachineSearchRSB {

        @Schema(description = "발급기 ID")
        private String mcnId;

        @Schema(description = "발급기 이름")
        private String mcnName;

        @Schema(description = "기타정보")
        private String etc;

        @Schema(description = "발급기의 디바이스 목록")
        private List<DeviceObj> deviceList;

        @Schema(description = "등록일자")
        private String createdAt;


    }

}


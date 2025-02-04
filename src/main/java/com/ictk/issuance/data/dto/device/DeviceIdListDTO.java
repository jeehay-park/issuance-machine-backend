package com.ictk.issuance.data.dto.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class DeviceIdListDTO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceIdListRSB {
        @Schema(description = "디바이스 ID 목록")
        private List<String> deviceIdList;
    }
}

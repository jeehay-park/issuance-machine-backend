package com.ictk.issuance.data.dto.machine;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class MachineIdListDTO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MachineIdListRSB {

        @Schema(description = "발급 기계 ID 목록")
        private List<String> machineIdList;
    }
}

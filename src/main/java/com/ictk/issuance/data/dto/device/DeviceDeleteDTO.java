package com.ictk.issuance.data.dto.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class DeviceDeleteDTO {

    /**
     * 발급기계 삭제
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class DeviceDeleteRQB {

        @Schema(description = "디바이스 IDs")
        @NotNull(message = "mcnId는 필수 값입니다.")
        private List<String> dvcIds;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceDeleteRSB {

        @Schema(description = "디바이스 삭제결과")
        private String result;

        @Schema(description = "디바이스 삭제개수")
        private int deleteCnt;

    }


}

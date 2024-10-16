package com.ictk.issuance.data.dto.machine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MachineDeleteDTO {

    /**
     * 발급기계 삭제
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class MachineDeleteRQB {

        @Schema(description = "발급기 ID")
        @NotNull(message = "mcnId는 필수 값입니다.")
        private String mcnId;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MachineDeleteRSB {

        @Schema(description = "발급기 삭제결과")
        private String result;

        @Schema(description = "발급기 삭제개수")
        private int deleteCnt;

    }

}

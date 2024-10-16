package com.ictk.issuance.data.dto.machine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MachineSaveDTO {

    /**
     * 발급기계 생성/갱신
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class MachineSaveRQB {

        @Schema(description = "발급기 ID", defaultValue = "mcn0001")
        private String mcnId;

        @Schema(description = "발급기 이름")
        @NotNull(message = "mcnName은 필수 값입니다.")
        private String mcnName;

        @Schema(description = "기타 정보")
        private String etc;

    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MachineSaveRSB {

        @Schema(description = "발급기 ID")
        private String mcnId;

    }

}

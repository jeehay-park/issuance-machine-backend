package com.ictk.issuance.data.dto.programinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ProgramInfoIdListDTO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProgramInfoIdListRSB {
        @Schema(description = "프로그램 ID 목록")
        private List<String> programInfoIdList;
    }

}

package com.ictk.issuance.data.dto.workinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class WorkIdListDTO {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkIdListRSB {
        @Schema(description = "작업 ID 목록")
        private List<String> workIdList;
    }
}

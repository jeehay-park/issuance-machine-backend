package com.ictk.issuance.data.dto.snrule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class SNRuleIdListDTO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SNRuleIdListRSB {

        @Schema(description = "SN 규칙 ID 목록")
        private List<String> sNRuleIdList;
    }
}

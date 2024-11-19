package com.ictk.issuance.data.dto.codeenum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;

public class CodeEnumListDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CodeEnumListRQB {
        private String codeId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CodeEnumListRSB {
        @Nullable
        private List<CodeEnumDTO.CodeEnumObj> enumList;
    }

}

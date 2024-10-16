package com.ictk.issuance.data.dto.shared;

import com.ictk.issuance.common.annotations.ValidateString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AppDTO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HeaderInfoObj {
        private int idx;
        private String keyName;
        private String name;
        private boolean isSort;
        private boolean isFilter;
        private boolean isDisplay;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FilterObj {
        private String keyName;
        @ValidateString(acceptedValues={"equals","contains", "not_equals"}, message="filterOp가 유효하지 않습니다.")
        private String filterOp;
        private String keyValue;
    }

}

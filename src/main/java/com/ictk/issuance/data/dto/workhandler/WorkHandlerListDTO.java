package com.ictk.issuance.data.dto.workhandler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ictk.issuance.common.annotations.ValidateString;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.shared.AppDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

public class WorkHandlerListDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WorkHandlerListRQB {

        @NotNull(message = "workId는 필수 값입니다.")
        @Schema(description = "발급작업 ID. 'wrk_' + seq의 형식")
        private String workId;

        @Schema(description = "핸들러 이름 (like 쿼리)")
        private String hdlName;

        @Schema(description = "디바이스 이름 ( like 쿼리)")
        private String dvcName;

        @JsonProperty("isHeaderInfo")
        @Builder.Default
        private boolean isHeaderInfo = true;

        @NotNull(message = "rowCnt는 필수 값입니다.")
        @Builder.Default
        private int rowCnt = AppConstants.DEFAULT_LOW_CNT;

        @NotNull(message = "startNum은 필수 값입니다.")
        private int startNum;

        private String sortKeyName;

        @ValidateString(acceptedValues={"ASC","DESC"}, message="order가 유효하지 않습니다.")
        private String order;


        public Pageable getPageable() {
            if(rowCnt <=0) rowCnt = AppConstants.DEFAULT_LOW_CNT;
            int page = (int)(startNum / rowCnt);
            return PageRequest.of(page, rowCnt);
        }


    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkHandlerListRSB {

        @NotEmpty(message = "totalCnt는 필수 값입니다.")
        private long totalCnt;

        private int curPage;

        @Nullable
        private List<AppDTO.HeaderInfoObj> headerInfos;

        private List<Map<String, Object>> workHandlers;
    }


}

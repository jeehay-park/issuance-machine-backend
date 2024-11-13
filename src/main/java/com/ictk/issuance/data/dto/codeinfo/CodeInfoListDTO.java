package com.ictk.issuance.data.dto.codeinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ictk.issuance.common.annotations.ValidateString;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.shared.AppDTO;
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


public class CodeInfoListDTO {

    // 코드 정보 목록 조회

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CodeInfoListRQB {

        @JsonProperty("isHeaderInfo")
        @Builder.Default
        private boolean isHeaderInfo = true;

        @NotNull(message = "rowCnt는 필수 값입니다.")
        @Builder.Default
        private int rowCnt = AppConstants.DEFAULT_LOW_CNT;

        @NotNull(message = "startNum는 필수 값입니다.")
        private int startNum;

        private String sortKeyName;

        @ValidateString(acceptedValues = {"ASC", "DESC"}, message = "order가 유효하지 않습니다.")
        private String order;

        private String codeName;

        private String codeGroup;

        private String filter;

        @ValidateString(acceptedValues={"AND", "OR"}, message="filterArrAndOr이 유효하지 않습니다.")
        private String filterArrAndOr;

        private List<AppDTO.FilterObj> filterArr;

        public Pageable getPageble() {
            if(rowCnt <=0) rowCnt = AppConstants.DEFAULT_LOW_CNT;
            int page = (int)(startNum / rowCnt);
            return PageRequest.of(page, rowCnt);
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CodeInfoListRSB {

        @NotEmpty(message = "totalCnt는 필수 값입니다.")
        private long totalCnt;

        private int curPage;

        @Nullable
        private List<AppDTO.HeaderInfoObj> headerInfos;

        private List<Map<String,Object>> codeInfoList;

    }


}

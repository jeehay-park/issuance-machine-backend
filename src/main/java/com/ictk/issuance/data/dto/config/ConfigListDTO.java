package com.ictk.issuance.data.dto.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ictk.issuance.common.annotations.ValidateString;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.shared.AppDTO.HeaderInfoObj;
import com.ictk.issuance.data.dto.shared.AppDTO.FilterObj;
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


public class ConfigListDTO {

    /**
     * 발급설정(프로파일) 목록 조회
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class ConfigListRQB {
        @JsonProperty("isHeaderInfo")
        @Builder.Default
        private boolean isHeaderInfo = true;

        @NotNull(message = "rowCnt는 필수 값입니다.")
        @Builder.Default
        private int rowCnt = AppConstants.DEFAULT_LOW_CNT;

        @NotNull(message = "startNum은 필수 값입니다.")
        private int startNum;

        // @Builder.Default
        private String sortKeyName;

        @ValidateString(acceptedValues={"ASC","DESC"}, message="order가 유효하지 않습니다.")
        private String order;

        @ValidateString(acceptedValues={"PROFILE","KEYISSUE","SCRIPT"}, message="configType이 유효하지 않습니다.")
        @NotNull(message = "configType은 필수 값입니다.")
        private String configType;

        private String name;

        private String filter;

        @ValidateString(acceptedValues={"AND", "OR"}, message="filterArrAndOr이 유효하지 않습니다.")
        private String filterArrAndOr;

        private List<FilterObj> filterArr;

        public Pageable getPageble() {
            if(rowCnt <=0) rowCnt = AppConstants.DEFAULT_LOW_CNT;
            int page = (int)(startNum / rowCnt);
            return PageRequest.of(page, rowCnt);
        }

        public List<FilterObj> getFilterArr() {
            return List.of();
        }
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfigListRSB {

        @NotEmpty(message = "totalCnt는 필수 값입니다.")
        private long totalCnt;

        private int curPage;

        @Nullable
        private List<HeaderInfoObj> headerInfos;

        private List<Map<String,Object>> configList;

    }

}

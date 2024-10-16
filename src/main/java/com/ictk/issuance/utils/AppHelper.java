package com.ictk.issuance.utils;

import com.google.common.base.CaseFormat;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.constants.IssuanceConstants;
import com.ictk.issuance.data.dto.shared.AppDTO.FilterObj;
import com.ictk.issuance.data.dto.shared.AppDTO.HeaderInfoObj;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Slf4j
public class AppHelper {

    public static Map<String, HeaderInfoObj> machineHeaderInfoMap() {

        Map<String, HeaderInfoObj> hdrInfoMap = new LinkedHashMap<>(); // 순서보장

        int idx = 1;
        hdrInfoMap.put("idx", HeaderInfoObj.builder().idx(idx++).keyName("idx").name("순번").isSort(false).isFilter(false).isDisplay(true).build());
        hdrInfoMap.put("mcn_id", HeaderInfoObj.builder().idx(idx++).keyName("mcn_id").name("발급기 ID").isSort(true).isFilter(false).isDisplay(true).build());
        hdrInfoMap.put("mcn_name", HeaderInfoObj.builder().idx(idx++).keyName("mcn_name").name("발급기 이름").isSort(false).isFilter(true).isDisplay(true).build());
        hdrInfoMap.put("etc", HeaderInfoObj.builder().idx(idx++).keyName("etc").name("기타 정보").isSort(false).isFilter(true).isDisplay(true).build());
        hdrInfoMap.put("created_at", HeaderInfoObj.builder().idx(idx++).keyName("created_at").name("등록 시간").isSort(true).isFilter(false).isDisplay(true).build());
        return hdrInfoMap;

    }

    public static Map<String, HeaderInfoObj> configHeaderInfoMap(String configType) {
        Map<String, HeaderInfoObj> hdrInfoMap = new LinkedHashMap<>();
        int idx = 1;
        switch(configType) {
            case IssuanceConstants.CONFIG_TYPE_PROFILE -> {
                hdrInfoMap.put("idx", HeaderInfoObj.builder().idx(idx++).keyName("idx").name("순번").isSort(false).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("prof_id", HeaderInfoObj.builder().idx(idx++).keyName("prof_id").name("프로파일 ID").isSort(true).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("prof_name", HeaderInfoObj.builder().idx(idx++).keyName("prof_name").name("프로파일 이름").isSort(false).isFilter(true).isDisplay(true).build());
                hdrInfoMap.put("description", HeaderInfoObj.builder().idx(idx++).keyName("description").name("상세설명").isSort(false).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("prof_type", HeaderInfoObj.builder().idx(idx++).keyName("prof_type").name("프로파일 타입").isSort(false).isFilter(false).isDisplay(false).build());
                hdrInfoMap.put("version", HeaderInfoObj.builder().idx(idx++).keyName("version").name("버전").isSort(false).isFilter(true).isDisplay(true).build());
                // hdrInfoMap.put("ctnt_data", HeaderInfoObj.builder().idx(idx++).keyName("ctnt_data").name("컨텐츠 데이터").isSort(false).isFilter(false).isDisplay(false).build());
                hdrInfoMap.put("data_hash", HeaderInfoObj.builder().idx(idx++).keyName("data_hash").name("데이터 해시").isSort(false).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("updated_at", HeaderInfoObj.builder().idx(idx++).keyName("updated_at").name("업데이트 시간").isSort(true).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("created_at", HeaderInfoObj.builder().idx(idx++).keyName("created_at").name("등록 시간").isSort(true).isFilter(false).isDisplay(true).build());
            }
            case IssuanceConstants.CONFIG_TYPE_KEYISSUE -> {
                hdrInfoMap.put("idx", HeaderInfoObj.builder().idx(idx++).keyName("idx").name("순번").isSort(false).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("keyis_id", HeaderInfoObj.builder().idx(idx++).keyName("keyis_id").name("키발급코드 ID").isSort(true).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("keyis_name", HeaderInfoObj.builder().idx(idx++).keyName("keyis_name").name("키발급코드 이름").isSort(false).isFilter(true).isDisplay(true).build());
                hdrInfoMap.put("description", HeaderInfoObj.builder().idx(idx++).keyName("description").name("상세설명").isSort(false).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("keyis_type", HeaderInfoObj.builder().idx(idx++).keyName("keyis_type").name("키발급코드 타입").isSort(false).isFilter(false).isDisplay(false).build());
                hdrInfoMap.put("version", HeaderInfoObj.builder().idx(idx++).keyName("version").name("버전").isSort(false).isFilter(true).isDisplay(true).build());
                // hdrInfoMap.put("ctnt_data", HeaderInfoObj.builder().idx(idx++).keyName("ctnt_data").name("컨텐츠 데이터").isSort(false).isFilter(false).isDisplay(false).build());
                hdrInfoMap.put("data_hash", HeaderInfoObj.builder().idx(idx++).keyName("data_hash").name("데이터 해시").isSort(false).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("updated_at", HeaderInfoObj.builder().idx(idx++).keyName("updated_at").name("업데이트 시간").isSort(true).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("created_at", HeaderInfoObj.builder().idx(idx++).keyName("created_at").name("등록 시간").isSort(true).isFilter(false).isDisplay(true).build());
            }
            case IssuanceConstants.CONFIG_TYPE_SCRIPT -> {
                hdrInfoMap.put("idx", HeaderInfoObj.builder().idx(idx++).keyName("idx").name("순번").isSort(false).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("scrt_id", HeaderInfoObj.builder().idx(idx++).keyName("scrt_id").name("스크립트 ID").isSort(true).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("scrt_name", HeaderInfoObj.builder().idx(idx++).keyName("scrt_name").name("스크립트 이름").isSort(false).isFilter(true).isDisplay(true).build());
                hdrInfoMap.put("description", HeaderInfoObj.builder().idx(idx++).keyName("description").name("상세설명").isSort(false).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("scrt_type", HeaderInfoObj.builder().idx(idx++).keyName("scrt_type").name("스크립트 타입").isSort(false).isFilter(false).isDisplay(false).build());
                hdrInfoMap.put("version", HeaderInfoObj.builder().idx(idx++).keyName("version").name("버전").isSort(false).isFilter(true).isDisplay(true).build());
                // hdrInfoMap.put("ctnt_data", HeaderInfoObj.builder().idx(idx++).keyName("ctnt_data").name("컨텐츠 데이터").isSort(false).isFilter(false).isDisplay(false).build());
                hdrInfoMap.put("data_hash", HeaderInfoObj.builder().idx(idx++).keyName("data_hash").name("데이터 해시").isSort(false).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("updated_at", HeaderInfoObj.builder().idx(idx++).keyName("updated_at").name("업데이트 시간").isSort(true).isFilter(false).isDisplay(true).build());
                hdrInfoMap.put("created_at", HeaderInfoObj.builder().idx(idx++).keyName("created_at").name("등록 시간").isSort(true).isFilter(false).isDisplay(true).build());
            }
        }
        return hdrInfoMap;

    }

    public static Map<String, HeaderInfoObj> snRuleHeaderInfoMap() {

        Map<String, HeaderInfoObj> hdrInfoMap = new LinkedHashMap<>(); // 순서보장

        int idx = 1;
        hdrInfoMap.put("idx", HeaderInfoObj.builder().idx(idx++).keyName("idx").name("순번").isSort(false).isFilter(false).isDisplay(true).build());
        hdrInfoMap.put("snr_id", HeaderInfoObj.builder().idx(idx++).keyName("snr_id").name("SN규칙 ID").isSort(true).isFilter(false).isDisplay(true).build());
        hdrInfoMap.put("snr_name", HeaderInfoObj.builder().idx(idx++).keyName("snr_name").name("SN규칙 이름").isSort(false).isFilter(true).isDisplay(true).build());
        hdrInfoMap.put("test_code", HeaderInfoObj.builder().idx(idx++).keyName("test_code").name("테스트 코드").isSort(false).isFilter(true).isDisplay(true).build());
        hdrInfoMap.put("location", HeaderInfoObj.builder().idx(idx++).keyName("location").name("로케이션").isSort(false).isFilter(false).isDisplay(true).build());
        // hdrInfoMap.put("last_burn_date", HeaderInfoObj.builder().idx(idx++).keyName("last_burn_date").name("최근 Burn 시간").isSort(false).isFilter(false).isDisplay(false).build());
        hdrInfoMap.put("today_count", HeaderInfoObj.builder().idx(idx++).keyName("today_count").name("당일 건수").isSort(false).isFilter(false).isDisplay(true).build());
        hdrInfoMap.put("count_sum", HeaderInfoObj.builder().idx(idx++).keyName("count_sum").name("건수 합계").isSort(true).isFilter(false).isDisplay(true).build());
        hdrInfoMap.put("created_at", HeaderInfoObj.builder().idx(idx++).keyName("created_at").name("등록 시간").isSort(true).isFilter(false).isDisplay(true).build());
        return hdrInfoMap;

    }



    public static Tuple2<String, List<FilterObj>>  applyFilterToRequestRQB(Map<String, HeaderInfoObj> hdrMap, String filter) {
        List<FilterObj> filterArr = new ArrayList<>();

        if(CommonUtils.hasStringValue(filter)) {
            hdrMap.forEach((k, v) -> {
                if (v.isFilter())
                    filterArr.add(new FilterObj(v.getKeyName(), AppConstants.FILTER_OP_CONTAINS, filter));
            });
        }
        return Tuple.of(AppConstants.FILTER_OR, filterArr);
    }

    public static List<OrderSpecifier> getRequestRQBOrderSpecifiers(
            Class<?> clsType, String clsName,
            Map<String, HeaderInfoObj> hdrInfoMap, String sortKeyName, String order ) {
        PathBuilder<?> entity = new PathBuilder<>(clsType, clsName);
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        HeaderInfoObj headerObj = hdrInfoMap.get(sortKeyName);
        if(headerObj != null && headerObj.isSort()) {
            String entityField = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, headerObj.getKeyName());
            orderSpecifiers.add(new OrderSpecifier(AppConstants.ORDER_DESC.equals(order)? Order.DESC:Order.ASC, entity.get( entityField )) );
        } else
            orderSpecifiers.add( new OrderSpecifier( Order.DESC, entity.get("createdAt")) );

        return orderSpecifiers;
    }


    public static BooleanBuilder composeFilterDynamicConditions(
            String andOr,
            List<FilterObj> filterObjs,
            Function<String, Path> getEntityPath) {

        return composeFilterDynamicConditions(andOr, filterObjs, getEntityPath, null);

    }

    public static BooleanBuilder composeFilterDynamicConditions(
            String andOr,
            List<FilterObj> filterObjs,
            Function<String, Path> getEntityPath,
            Function<String, Class> getEnumClass) {

        if(filterObjs == null)
            return null;

        BooleanBuilder queryConds = new BooleanBuilder();

        filterObjs.forEach( filterObj -> {
            Path qPath = getEntityPath.apply(filterObj.getKeyName());
            if( qPath instanceof StringPath) {
                StringPath stringPath = (StringPath)qPath;
                switch (filterObj.getFilterOp()) {
                    case "equals" -> {
                        if (AppConstants.FILTER_AND.equals(andOr))
                            queryConds.and(stringPath.eq(filterObj.getKeyValue()));
                        else
                            queryConds.or(stringPath.eq(filterObj.getKeyValue()));
                    }
                    case "not_equals" -> {
                        if (AppConstants.FILTER_AND.equals(andOr))
                            queryConds.and(stringPath.ne(filterObj.getKeyValue()));
                        else
                            queryConds.or(stringPath.ne(filterObj.getKeyValue()));
                    }
                    case "contains" -> {
                        if (AppConstants.FILTER_AND.equals(andOr))
                            queryConds.and(stringPath.contains(filterObj.getKeyValue()));
                        else
                            queryConds.or(stringPath.contains(filterObj.getKeyValue()));
                    }
                    case "not_contains" -> {
                        if (AppConstants.FILTER_AND.equals(andOr))
                            queryConds.and(stringPath.notLike("%" + filterObj.getKeyValue() + "%"));
                        else
                            queryConds.or(stringPath.notLike("%" + filterObj.getKeyValue() + "%"));
                    }
                }
            }
            else if( qPath instanceof EnumPath && getEnumClass!=null ) {
                EnumPath enumPath = (EnumPath)qPath;
                switch (filterObj.getFilterOp()) {
                    case "equals" -> {

                        if (AppConstants.FILTER_AND.equals(andOr))
                            queryConds.and(enumPath.eq( CommonUtils.getEnum(getEnumClass.apply(filterObj.getKeyName()), filterObj.getKeyValue()) ));
                        else {
                            queryConds.or(enumPath.eq( CommonUtils.getEnum(getEnumClass.apply(filterObj.getKeyName()), filterObj.getKeyValue()) ));
                        }
                    }
                    case "not_equals" -> {
                        if (AppConstants.FILTER_AND.equals(andOr))
                            queryConds.and(enumPath.ne( CommonUtils.getEnum(getEnumClass.apply(filterObj.getKeyName()), filterObj.getKeyValue()) ));
                        else
                            queryConds.or(enumPath.ne( CommonUtils.getEnum(getEnumClass.apply(filterObj.getKeyName()), filterObj.getKeyValue()) ));
                    }
                }
            }
        } );

        return queryConds;

    }

}

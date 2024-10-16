package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.model.ScriptConfig;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScriptConfigDao {

    // 스크립트 설정 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 스크립트 설정 조회 (페이징)
    Tuple2<Long, Page<ScriptConfig>> getScriptConfigPageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers);

    // 스크립트 설정 삭제
    long deleteScriptConfigByScrtId(String scrtId);

}

package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.model.KeyissueConfig;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KeyissueConfigDao {

    // 키발급코드 설정 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 키발급코드 설정 조회 (페이징)
    Tuple2<Long, Page<KeyissueConfig>> getKeyissueConfigPageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers);

    // 키발급코드 설정 삭제
    long deleteKeyissueConfigByKeyisId(String keyisId);

    List<String> findAllKeyisIds();

}

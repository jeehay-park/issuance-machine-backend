package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.model.SNRule;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SNRuleDao {

    // 시리얼넘버 규칙 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 시리얼넘버 규칙 조회 (페이징)
    Tuple2<Long, Page<SNRule>> getSNRulePageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers);

    // 시리얼넘버 규칙 삭제
    long deleteSNRuleBySnrId(String snrId);

    // SN 규칙 Id 목록
    List<String> findAllSnrIds();

}

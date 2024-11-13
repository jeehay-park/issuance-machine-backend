package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.model.CodeInfo;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CodeInfoDao {

    // 코드 정보 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 코드 정보 조회 (페이징)
    Tuple2<Long, Page<CodeInfo>> getCodeInfoPageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers
    );

    // 코드 정보 삭제
    long deleteCodeInfoByCodeId(String codeId);

}

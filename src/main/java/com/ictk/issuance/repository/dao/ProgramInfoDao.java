package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.model.ProgramInfo;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProgramInfoDao {

    // 프로그램 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 프로그램 삭제
    long deleteProgramProgId(String progId);

    // 프로그램 조회 (페이징)
    Tuple2<Long, Page<ProgramInfo>> getProgramInfoPageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers);



    // Program Id 목록
    List<String> findAllProgIds();
}

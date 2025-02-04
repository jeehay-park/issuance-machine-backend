package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.model.ProgramInfo;
import com.ictk.issuance.data.model.WorkInfo;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkInfoDao {

    // 발급 작업(work) 정보를 관리하는 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 발급 작업 삭제
    long deleteWorkId(String workId);

    // 발급 작업 조회 (페이징)
    Tuple2<Long, Page<WorkInfo>> getWorkInfoPageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers);

    // 작업 Id 목록
    List<String> findAllWorkIds();

}


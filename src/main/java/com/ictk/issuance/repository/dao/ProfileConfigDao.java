package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.model.ProfileConfig;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProfileConfigDao {

    // 발급 프로파일 설정 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 발급 프로파일 조회 (페이징)
    Tuple2<Long, Page<ProfileConfig>> getProfileConfigPageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers);

    // 발급 프로파일 설정 삭제
    long deleteProfileConfigByProfId(String profId);

    // ProfId 목록 조회
    List<String> findAllProfIds();
}

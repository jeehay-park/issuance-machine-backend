package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.model.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserDao {

    // user 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 사용자 목록 조회 (페이징)
    Tuple2<Long, Page<User>> getUserPageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers);

    // 사용자 삭제
    long deleteUserByUserId(String userId);
}

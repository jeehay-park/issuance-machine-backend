package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.model.Machine;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MachineDao {

    // 머신 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 머신 etc 변경
    // @Transactional
    long updateEtc(String mcnId, String etc);

    // 머신 조회 (페이징)
    Tuple2<Long, Page<Machine>> getMachinePageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers);

    // 머신 삭제
    long deleteMachineByMcnId(String mcnId);

}

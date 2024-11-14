package com.ictk.issuance.repository.dao;

import org.springframework.transaction.annotation.Transactional;

public interface CodeEnumDao {

    // ENUM 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // codeID로 삭제
    @Transactional
    long deleteCodeEnumByCodeId(String codeId);
}

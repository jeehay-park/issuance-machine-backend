package com.ictk.issuance.repository.dao;

public interface CodeEnumDao {

    // ENUM 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // codeID로 삭제

    long deleteEnumByCodeId(String codeId);

}

package com.ictk.issuance.repository.dao;

public interface IssuanceDao {

    // 테이블 체크
    boolean isTableExist(String tableSchema, String tableName);

}

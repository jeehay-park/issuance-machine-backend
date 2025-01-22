package com.ictk.issuance.repository.dao;

public interface WorkDetailDao {
    // 발급 작업(work) 상세 테이블 생성하기
    boolean makeTable(String database, String tableName);

}

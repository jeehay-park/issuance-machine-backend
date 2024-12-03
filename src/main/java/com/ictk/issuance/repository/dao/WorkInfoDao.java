package com.ictk.issuance.repository.dao;

public interface WorkInfoDao {

    // 발급 작업(work) 정보를 관리하는 테이블 생성하기
    boolean makeTable(String database, String tableName);

}

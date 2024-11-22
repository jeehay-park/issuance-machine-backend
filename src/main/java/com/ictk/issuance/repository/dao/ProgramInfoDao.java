package com.ictk.issuance.repository.dao;

public interface ProgramInfoDao {

    // 프로그램 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 프로그램 삭제
    long deleteProgramProgId(String progId);
}

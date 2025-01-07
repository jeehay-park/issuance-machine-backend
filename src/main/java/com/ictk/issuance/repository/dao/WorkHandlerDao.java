package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.model.WorkHandler;

import java.util.List;
import java.util.Optional;

public interface WorkHandlerDao {

    // 발급 작업(work) 핸들러를 관리하는 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 발급 작업 핸들러 삭제
    long deleteWorkHandlerId(String hdlId);

    // workId로 WorkHandler 조회
    Optional<WorkHandler> findWorkHandlerByWorkId(String workId);

}

package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.dto.codeenum.CodeEnumDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CodeEnumDao {

    // ENUM 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // codeID로 삭제
    @Transactional
    long deleteCodeEnumByCodeId(String codeId);

    // codeId로 조회
    @Transactional(readOnly = true)
    @Query("SELECT new com.ictk.issuance.data.dto.codeenum.CodeEnumDTO$CodeEnumObj(e.enumValue, e.isMandatory, e.ip, e.description) " +
            "FROM CodeEnum e WHERE e.codeInfo.codeId = :codeId")
    public List<CodeEnumDTO.CodeEnumObj> findAllByCodeId(@Param("codeId") String codeId);
}

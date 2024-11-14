package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.CodeEnum;
import com.ictk.issuance.repository.dao.CodeEnumDao;
import com.ictk.issuance.repository.dao.IssuanceDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeEnumRepository extends JpaRepository<CodeEnum, String>, CodeEnumDao, IssuanceDao {
    //codeId로 CodeEnum 조회
    Optional<CodeEnum> findCodeEnumByCodeId(String codeId);

}

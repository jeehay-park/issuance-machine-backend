package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.CodeEnum;
import com.ictk.issuance.repository.dao.CodeEnumDao;
import com.ictk.issuance.repository.dao.IssuanceDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeEnumRepository extends JpaRepository<CodeEnum, String>, CodeEnumDao, IssuanceDao {
    //enumId로 Enum 조회
    Optional<CodeEnum> findEnumByEnumId(String enumId);

}

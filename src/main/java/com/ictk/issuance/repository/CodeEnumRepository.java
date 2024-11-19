package com.ictk.issuance.repository;

import com.ictk.issuance.data.dto.codeenum.CodeEnumDTO;
import com.ictk.issuance.data.model.CodeEnum;
import com.ictk.issuance.repository.dao.CodeEnumDao;
import com.ictk.issuance.repository.dao.IssuanceDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeEnumRepository extends JpaRepository<CodeEnum, String>, CodeEnumDao, IssuanceDao {
    // codeId로 CodeEnum 조회
    // If codeId should ideally return one record but occasionally doesn't (edge case handling):
    // Optional<CodeEnum> findCodeEnumByCodeId(String codeId);

    List<CodeEnumDTO.CodeEnumObj> findAllByCodeId(String codeId);

}

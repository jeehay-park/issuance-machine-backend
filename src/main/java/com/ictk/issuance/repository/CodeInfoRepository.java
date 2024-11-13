package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.CodeInfo;
import com.ictk.issuance.repository.dao.CodeInfoDao;
import com.ictk.issuance.repository.dao.IssuanceDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeInfoRepository extends JpaRepository<CodeInfo, String>, CodeInfoDao, IssuanceDao{

    // ID로 코드 정보 조회
    Optional<CodeInfo> findCodeInfoByCodeId(String codeId);


}

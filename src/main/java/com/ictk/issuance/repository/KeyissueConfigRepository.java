package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.KeyissueConfig;
import com.ictk.issuance.repository.dao.IssuanceDao;
import com.ictk.issuance.repository.dao.KeyissueConfigDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface KeyissueConfigRepository extends JpaRepository<KeyissueConfig, String>, KeyissueConfigDao, IssuanceDao {

    // 키발급코드 ID로 KeyissueConfig 조회
    Optional<KeyissueConfig> findKeyissueConfigByKeyisId(String keyisId);

}

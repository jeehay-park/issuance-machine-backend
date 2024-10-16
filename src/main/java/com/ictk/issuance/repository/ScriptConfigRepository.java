package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.ScriptConfig;
import com.ictk.issuance.repository.dao.IssuanceDao;
import com.ictk.issuance.repository.dao.ScriptConfigDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ScriptConfigRepository extends JpaRepository<ScriptConfig, String>, ScriptConfigDao, IssuanceDao {

    // 스크립트 ID로 ScriptConfig 조회
    Optional<ScriptConfig> findScriptConfigByScrtId(String scrtId);

}


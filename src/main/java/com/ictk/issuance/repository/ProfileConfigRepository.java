package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.ProfileConfig;
import com.ictk.issuance.repository.dao.IssuanceDao;
import com.ictk.issuance.repository.dao.ProfileConfigDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProfileConfigRepository extends JpaRepository<ProfileConfig, String>, ProfileConfigDao, IssuanceDao {

    // profId로 ProfileConfig 조회
    Optional<ProfileConfig> findProfileConfigByProfId(String profId);

}

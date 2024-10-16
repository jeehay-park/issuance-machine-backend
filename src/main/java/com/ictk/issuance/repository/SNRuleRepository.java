package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.SNRule;
import com.ictk.issuance.repository.dao.IssuanceDao;
import com.ictk.issuance.repository.dao.SNRuleDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SNRuleRepository extends JpaRepository<SNRule, String>, SNRuleDao, IssuanceDao {

    // SN규칙 ID로 SNRule 조회
    Optional<SNRule> findSNRuleBySnrId(String snrId);


}

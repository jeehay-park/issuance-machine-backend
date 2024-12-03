package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.WorkInfo;
import com.ictk.issuance.repository.dao.IssuanceDao;
import com.ictk.issuance.repository.dao.WorkInfoDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkInfoRepository extends JpaRepository<WorkInfo, String>, IssuanceDao, WorkInfoDao {

    // CRUD 외 기타 custom methods 정의


}

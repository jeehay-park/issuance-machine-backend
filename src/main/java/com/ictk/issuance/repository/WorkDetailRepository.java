package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.WorkDetail;
import com.ictk.issuance.repository.dao.IssuanceDao;
import com.ictk.issuance.repository.dao.WorkDetailDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkDetailRepository extends JpaRepository<WorkDetail, String>, IssuanceDao, WorkDetailDao {
    // CRUD 외 기타 custom methods 정의

}

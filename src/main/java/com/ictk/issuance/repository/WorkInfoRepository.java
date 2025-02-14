package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.WorkInfo;
import com.ictk.issuance.repository.dao.IssuanceDao;
import com.ictk.issuance.repository.dao.WorkInfoDao;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkInfoRepository extends JpaRepository<WorkInfo, String>, IssuanceDao, WorkInfoDao {

    // CRUD 외 기타 custom methods 정의

    @Modifying
    @Transactional
    @Query("UPDATE WorkInfo w SET w.status = :status WHERE w.workId = :workId")
    // work control 명령에 따라 workInfo status 업데이트
    int updateWorkStatusByWorkId(@Param("workId") String workId, @Param("status") String status);


}

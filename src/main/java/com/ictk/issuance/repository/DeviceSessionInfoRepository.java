package com.ictk.issuance.repository;
import com.ictk.issuance.data.model.DeviceSessionInfo;
import com.ictk.issuance.repository.dao.DeviceSessionInfoDao;
import com.ictk.issuance.repository.dao.IssuanceDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceSessionInfoRepository extends JpaRepository<DeviceSessionInfo, String>, IssuanceDao, DeviceSessionInfoDao {
    // CRUD 외 기타 custom methods 정의
}

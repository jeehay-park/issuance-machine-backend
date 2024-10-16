package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.Device;
import com.ictk.issuance.repository.dao.DeviceDao;
import com.ictk.issuance.repository.dao.IssuanceDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, String>, DeviceDao, IssuanceDao {

    // dvcId로 Device 조회
    Optional<Device> findDeviceByDvcId(String dvcId);

}

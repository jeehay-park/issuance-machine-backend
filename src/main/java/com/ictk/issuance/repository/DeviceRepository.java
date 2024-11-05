package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.Device;
import com.ictk.issuance.repository.dao.DeviceDao;
import com.ictk.issuance.repository.dao.IssuanceDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, String>, DeviceDao, IssuanceDao {

    // dvcId로 Device 조회
    Optional<Device> findDeviceByDvcId(String dvcId);

    // Find max dvcNum for a given mcnId
    @Query("SELECT COALESCE(MAX(d.dvcNum), 0) FROM Device d WHERE d.machine.mcnId = :mcnId")
    Optional<Integer> findMaxDvcNumByMcnId(@Param("mcnId") String mcnId);
}

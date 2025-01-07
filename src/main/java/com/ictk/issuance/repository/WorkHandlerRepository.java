package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.Device;
import com.ictk.issuance.data.model.KeyissueConfig;
import com.ictk.issuance.data.model.WorkHandler;
import com.ictk.issuance.repository.dao.IssuanceDao;
import com.ictk.issuance.repository.dao.WorkHandlerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkHandlerRepository extends JpaRepository<WorkHandler, String>, IssuanceDao, WorkHandlerDao {

    @Query("SELECT d FROM Device d WHERE d.dvcId = :dvcId")
    Optional<Device> findMcnIdByDvcId(@Param("dvcId") String dvcId);


}

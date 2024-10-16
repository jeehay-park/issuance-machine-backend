package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.Machine;
import com.ictk.issuance.repository.dao.IssuanceDao;
import com.ictk.issuance.repository.dao.MachineDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MachineRepository extends JpaRepository<Machine, String>, MachineDao, IssuanceDao {

    // mcnId로 Machine 조회
    Optional<Machine> findMachineByMcnId(String mcnId);

}

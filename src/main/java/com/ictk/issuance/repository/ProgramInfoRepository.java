package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.ProgramInfo;
import com.ictk.issuance.repository.dao.IssuanceDao;
import com.ictk.issuance.repository.dao.ProgramInfoDao;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProgramInfoRepository extends JpaRepository<ProgramInfo, String>, ProgramInfoDao, IssuanceDao {


}

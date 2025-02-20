package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.CodeInfo;
import com.ictk.issuance.repository.dao.CodeInfoDao;
import com.ictk.issuance.repository.dao.IssuanceDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// String is the type of the primary key of the CodeInfo entity.
// This means that the primary key of CodeInfo is of type String.
// So, CodeInfoRepository can handle operations on CodeInfo objects, where the primary key is a String.
// With JpaRepository<CodeInfo, String>, you get methods like:
// save(), findById(), deleteById(), findAll(), etc.,
// automatically without having to implement them yourself.

// The CodeInfoRepository interface extends three interfaces in total:

public interface CodeInfoRepository extends JpaRepository<CodeInfo, String>, CodeInfoDao, IssuanceDao{

    // The repository now has:
    // - CRUD operations from JpaRepository
    // - Custom query methods from CodeInfoDao
    // - Custom issuance-related methods from IssuanceDao

    // ID로 코드 정보 조회
    Optional<CodeInfo> findCodeInfoByCodeId(String codeId);
}

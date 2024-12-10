package com.ictk.issuance.repository.impl;


import com.ictk.issuance.repository.dao.IssuanceDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class IssuanceDaoImpl implements IssuanceDao {

    @Autowired
    private EntityManager entityManager;

    // 테이블 체크
    public boolean isTableExist(String tableSchema, String tableName) {
        Query nativeQuery =
                entityManager.createNativeQuery("SELECT count(*) FROM information_schema.tables WHERE table_schema = ? and table_name = ?" )
                        .setParameter(1, tableSchema)
                        .setParameter(2, tableName);
        long count = (Long) nativeQuery.getSingleResult();
        return count>0;
    }

}

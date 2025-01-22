package com.ictk.issuance.repository.impl;

import com.ictk.issuance.repository.dao.WorkDetailDao;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor
public class WorkDetailRepositoryImpl extends IssuanceDaoImpl implements WorkDetailDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean makeTable(String database, String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append(" CREATE TABLE IF NOT EXISTS `" + tableName + "` ( \n");
        sbSQL.append("  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작', \n");
        sbSQL.append("  `workdet_id` varchar(32) NOT NULL COMMENT '작업 상세 ID wrkdet_ + seq의 형식', \n");
        sbSQL.append("  `work_id` varchar(32) NOT NULL COMMENT '발급작업 ID wrk_ + seq의 형식', \n");
        sbSQL.append("  `detail_data` text DEFAULT NULL COMMENT '작업 상세 데이터. 발급 시 유용한 정보를 저장 zip압축후 base64로 변환', \n");
        sbSQL.append("  `updated_at` datetime NOT NULL COMMENT '작업 업데이트 시간', \n");
        sbSQL.append("  `created_at` datetime NOT NULL COMMENT '작업 업데이트 시간', \n");
        sbSQL.append("  `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("   PRIMARY KEY (`workdet_id`), \n");
        sbSQL.append("   UNIQUE KEY `IDX_SN_RULE_01_UK` (`seq`) \n");
        sbSQL.append("  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");

        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }
}

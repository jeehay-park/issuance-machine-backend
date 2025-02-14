package com.ictk.issuance.repository.impl;

import com.ictk.issuance.repository.dao.UserDao;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryImpl extends IssuanceDaoImpl implements UserDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean makeTable(String database, String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append("CREATE TABLE IF NOT EXISTS `" + tableName + "` ( \n");

        sbSQL.append(" `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',  \n");
        sbSQL.append(" `enum_seq` int(8) NOT NULL COMMENT '코드에 따른 ENUM의 순번', \n");
        sbSQL.append(" `enum_id` varchar(32) NOT NULL COMMENT '코드별 ENUM ID.code_id_ + enum_seq의 형식', \n");
        sbSQL.append(" `code_id` varchar(32) NOT NULL COMMENT '코드 ID. cd_ + seq의 형식', \n");
        sbSQL.append(" `enum_value` varchar(64) NOT NULL COMMENT '코드별 ENUM 값', \n");
        sbSQL.append(" `is_mandatory` char(1) NOT NULL COMMENT '필수여부 Y, N', \n");
        sbSQL.append(" `order` int(8) NOT NULL COMMENT '코드별 ENUM 표시 순서', \n");
        sbSQL.append(" `description`varchar(255) DEFAULT NULL COMMENT '코드 ENUM에 대한 상세설명', \n");
        sbSQL.append(" `ip`varchar(255) DEFAULT NULL COMMENT 'ip에 대한 상세설명', \n");
        sbSQL.append("PRIMARY KEY (`enum_id`), \n");
        sbSQL.append("UNIQUE KEY `IDX_CODE_ENUM_01_UK` (`seq`) \n");
        sbSQL.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");

        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }
}

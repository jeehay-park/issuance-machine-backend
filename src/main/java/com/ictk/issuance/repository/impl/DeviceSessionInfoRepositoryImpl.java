package com.ictk.issuance.repository.impl;

import com.ictk.issuance.repository.dao.DeviceSessionInfoDao;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor
public class DeviceSessionInfoRepositoryImpl extends IssuanceDaoImpl implements DeviceSessionInfoDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean makeTable(String database, String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append(" CREATE TABLE IF NOT EXISTS `" + tableName + "` ( \n");
        sbSQL.append("  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작', \n");
        sbSQL.append("  `sess_id` varchar(32) NOT NULL COMMENT '발급작업 핸들러 ID. sess_ + seq의 형식', \n");
        sbSQL.append("  `work_id` varchar(32) NOT NULL COMMENT '발급작업 ID. wrk_ + seq의 형식', \n");
        sbSQL.append("  `workdet_id` varchar(32) DEFAULT NULL COMMENT '작업 상세 ID. wrkdet_ + seq의 형식', \n");
        sbSQL.append("  `dvc_id` varchar(32) NOT NULL COMMENT '발급기 디바이스 고유 ID. dvc_ + seq의 형식', \n");
        sbSQL.append("  `session_date` datetime NOT NULL COMMENT '발급 세션 시간', \n");
        sbSQL.append("  `session_no` varchar(64) DEFAULT NULL COMMENT '세션의 고유 넘버', \n");
        sbSQL.append("  `chip_sn` varchar(64) DEFAULT NULL COMMENT '부여된 칩의 SN', \n");
        sbSQL.append("  `status` varchar(32) DEFAULT NULL COMMENT '발급 세션 상태', \n");
        sbSQL.append("  `result` varchar(32) DEFAULT NULL COMMENT '발급 세션 결과', \n");
        sbSQL.append("  `error` text DEFAULT NULL COMMENT '발급 실패시 에러 정보', \n");
        sbSQL.append("  `stk_msec_time` int(10) DEFAULT NULL COMMENT '발급 소요시간 (초)', \n");
        sbSQL.append("  `param` varchar(128) DEFAULT NULL COMMENT '파라미터', \n");
        sbSQL.append("  `param_ext` mediumtext DEFAULT NULL COMMENT '파라미터 확장', \n");
        sbSQL.append("  `updated_at` datetime NOT NULL COMMENT '작업 업데이트 시간 ', \n");
        sbSQL.append("  `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("  PRIMARY KEY (`sess_id`), \n");
        sbSQL.append("  UNIQUE KEY `IDX_PROGRAM_INFO_UK` (`seq`) \n");
        sbSQL.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");


        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }
}

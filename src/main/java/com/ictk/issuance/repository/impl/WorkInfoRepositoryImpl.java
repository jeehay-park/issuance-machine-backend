package com.ictk.issuance.repository.impl;

import com.ictk.issuance.data.model.WorkInfo;
import com.ictk.issuance.repository.dao.WorkInfoDao;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ictk.issuance.data.model.QProgramInfo.programInfo;
import static com.ictk.issuance.data.model.QWorkInfo.workInfo;

@Slf4j
@RequiredArgsConstructor
public class WorkInfoRepositoryImpl extends IssuanceDaoImpl implements WorkInfoDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean makeTable(String database, String tableName) {

        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append(" CREATE TABLE IF NOT EXISTS `"+tableName+"` ( \n");
        sbSQL.append("  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작', \n");
        sbSQL.append("  `work_id` varchar(32) NOT NULL COMMENT '발급작업 ID. wrk_ + seq의 형식', \n");
        sbSQL.append("  `work_no` varchar(128) DEFAULT NULL COMMENT '발급작업 표시 넘버', \n");
        sbSQL.append("  `tag_name` varchar(128) DEFAULT NULL COMMENT '태그 명', \n");
        sbSQL.append("  `customer` varchar(64) DEFAULT NULL COMMENT '고객', \n");
        sbSQL.append("  `device_name` varchar(32) DEFAULT NULL COMMENT '디바이스 이름', \n");
        sbSQL.append("  `order_no` varchar(16) DEFAULT NULL COMMENT '오더 넘버', \n");
        sbSQL.append("  `prog_id` varchar(32) NOT NULL COMMENT '프로그램 ID. 'prog_' + seq의 형식', \n");
        sbSQL.append("  `mcn_id` varchar(32) NOT NULL COMMENT '발급머신 ID. 'mcn_' + seq의 형식', \n");
        sbSQL.append("  `snr_id` varchar(32) NOT NULL COMMENT 'SN규칙 ID. 'snr_' + seq의 형식', \n");
        sbSQL.append("  `target_size` int(11) DEFAULT NULL COMMENT '발급 목표 수량', \n");
        sbSQL.append("  `completed_size` int(11) DEFAULT NULL COMMENT '발급 진행수량', \n");
        sbSQL.append("  `failed_size` int(11) DEFAULT NULL COMMENT '발급 실패(오류)수량', \n");
        sbSQL.append("  `check_size` int(11) DEFAULT NULL COMMENT '발급 검증수량', \n");
        sbSQL.append("  `due_date` datetime DEFAULT NULL COMMENT '작업완료 예정 시간', \n");
        sbSQL.append("  `description` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("  `is_lock` char(1) NOT NULL COMMENT '발급칩의 LOCK 여부', \n");
        sbSQL.append("  `status` varchar(32) NOT NULL COMMENT '작업 상태 ', \n");
        sbSQL.append("  `param` varchar(128) DEFAULT NULL COMMENT '작업 상태 ', \n");
        sbSQL.append("  `param_ext` mediumtext DEFAULT NULL COMMENT '파라미터 확장 ', \n");
        sbSQL.append("  `detail_msg` text DEFAULT NULL COMMENT '작업 상세 메시지 ', \n");
        sbSQL.append("  `started_at` datetime DEFAULT NULL COMMENT '작업 시작 시간 ', \n");
        sbSQL.append("  `updated_at` datetime NOT NULL COMMENT '작업 업데이트 시간 ', \n");
        sbSQL.append("  `created_at` datetime NOT NULL COMMENT '작업 업데이트 시간 ', \n");
        sbSQL.append("  `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("  PRIMARY KEY (`work_id`), \n");
        sbSQL.append("  UNIQUE KEY `IDX_SN_RULE_01_UK` (`seq`) \n");
        sbSQL.append("  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");

        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }

    @Override
    @Transactional
    public long deleteWorkId(String workId) {
        return jpaQueryFactory
                .delete(workInfo)
                .where(workInfo.workId.eq(workId)).execute();
    }

    @Override
    public Tuple2<Long, Page<WorkInfo>> getWorkInfoPageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers) {

        // total count 구하기

        JPAQuery<Long> countQuery
                = jpaQueryFactory
                .select(workInfo.count())
                .from(workInfo)
                .where(queryConds);

        Long total = countQuery.fetchCount();

        return Tuple.of(
                total,
                new PageImpl<>(
                        jpaQueryFactory.selectFrom(workInfo)
                                .where(queryConds)
                                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]))
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch(),
                        pageable,
                        total ));
    }
}

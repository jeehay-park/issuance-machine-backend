package com.ictk.issuance.repository.impl;

import com.ictk.issuance.data.model.KeyissueConfig;
import com.ictk.issuance.repository.dao.KeyissueConfigDao;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ictk.issuance.data.model.QKeyissueConfig.keyissueConfig;


@Slf4j
@RequiredArgsConstructor
public class KeyissueConfigRepositoryImpl extends IssuanceDaoImpl implements KeyissueConfigDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean makeTable(String database, String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append("CREATE TABLE IF NOT EXISTS `"+tableName+"` ( \n");
        sbSQL.append("  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작', \n");
        sbSQL.append("  `keyis_id` varchar(32) NOT NULL COMMENT '키발급코드 ID. kis_ + seq 의 형식', \n");
        sbSQL.append("  `keyis_name` varchar(256) NOT NULL COMMENT '키발급코드 이름', \n");
        sbSQL.append("  `description` varchar(256) DEFAULT NULL COMMENT '키발급코드 상세 설명', \n");
        sbSQL.append("  `keyis_type` varchar(32) DEFAULT NULL COMMENT '키발급코드 타입(필요시)', \n");
        sbSQL.append("  `version` varchar(32) DEFAULT NULL COMMENT '버전 ex: 2.05', \n");
        sbSQL.append("  `ctnt_data` text DEFAULT NULL COMMENT '키발급코드 컨텐츠 데이터', \n");
        sbSQL.append("  `data_hash` varchar(64) DEFAULT NULL COMMENT '키발급코드 데이터 해시', \n");
        sbSQL.append("  `updated_at` datetime NOT NULL COMMENT '업데이트 시간', \n");
        sbSQL.append("  `created_at` datetime NOT NULL COMMENT '등록 시간', \n");
        sbSQL.append("  `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("  PRIMARY KEY (`keyis_id`), \n");
        sbSQL.append("  UNIQUE KEY `IDX_KEYISSUE_CONFIG_UK` (`seq`) \n");
        sbSQL.append("  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");

        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }

    // 키발급코드 설정 조회 (페이징)
    @Override
    public Tuple2<Long, Page<KeyissueConfig>> getKeyissueConfigPageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers) {

        // total count 구하기
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(keyissueConfig.count())
                .from(keyissueConfig)
                .where(queryConds);

        Long total = countQuery.fetchCount();

        return Tuple.of(
                total,
                new PageImpl<>(
                        jpaQueryFactory.selectFrom(keyissueConfig)
                                .where(queryConds)
                                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]))
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch(),
                        pageable,
                        total ));

    }

    // 키발급코드 설정 삭제
    @Override
    @Transactional
    public long deleteKeyissueConfigByKeyisId(String keyisId) {
        return jpaQueryFactory
                .delete(keyissueConfig)
                .where(keyissueConfig.keyisId.eq(keyisId))
                .execute();
    }

    @Override
    public List<String> findAllKeyisIds() {
        return jpaQueryFactory
                .select(keyissueConfig.keyisId)
                .from(keyissueConfig)
                .fetch();
    }

}

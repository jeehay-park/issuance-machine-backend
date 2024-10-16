package com.ictk.issuance.repository.impl;

import com.ictk.issuance.data.model.SNRule;
import com.ictk.issuance.repository.dao.SNRuleDao;
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

import static com.ictk.issuance.data.model.QSNRule.sNRule;


@Slf4j
@RequiredArgsConstructor
public class SNRuleRepositoryImpl extends IssuanceDaoImpl implements SNRuleDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    // 시리얼넘버 규칙 테이블 생성하기
    @Override
    public boolean makeTable(String database, String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append(" CREATE TABLE IF NOT EXISTS `"+tableName+"` ( \n");
        sbSQL.append("  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작', \n");
        sbSQL.append("  `snr_id` varchar(32) NOT NULL COMMENT 'SN 규칙 ID. snr_ + seq의 형식', \n");
        sbSQL.append("  `snr_name` varchar(64) DEFAULT NULL COMMENT 'SN 규칙 이름 (대상 제품)', \n");
        sbSQL.append("  `test_code` varchar(64) DEFAULT NULL COMMENT '테스트 코드', \n");
        sbSQL.append("  `location` int(8) DEFAULT NULL COMMENT '작업 위치', \n");
        sbSQL.append("  `last_burn_date` datetime DEFAULT NULL COMMENT '최근 Burn 시간', \n");
        sbSQL.append("  `today_count` int(8) DEFAULT NULL COMMENT '금일 건수', \n");
        sbSQL.append("  `count_sum` int(8) DEFAULT NULL COMMENT '건수 합계', \n");
        sbSQL.append("  `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간', \n");
        sbSQL.append("  `created_at` datetime NOT NULL COMMENT '등록 시간', \n");
        sbSQL.append("  `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("  PRIMARY KEY (`snr_id`), \n");
        sbSQL.append("  UNIQUE KEY `IDX_SN_RULE_01_UK` (`seq`) \n");
        sbSQL.append("  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");

        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }

    @Override
    // 시리얼넘버 규칙 조회 (페이징)
    public Tuple2<Long, Page<SNRule>> getSNRulePageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers) {

        // total count 구하기
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(sNRule.count())
                .from(sNRule)
                .where(queryConds);

        Long total = countQuery.fetchCount();

        return Tuple.of(
                total,
                new PageImpl<>(
                        jpaQueryFactory.selectFrom(sNRule)
                                .where(queryConds)
                                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]))
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch(),
                        pageable,
                        total ));

    }

    @Override
    @Transactional
    // 시리얼넘버 규칙 삭제
    public long deleteSNRuleBySnrId(String snrId) {
        return jpaQueryFactory
                .delete(sNRule)
                .where(sNRule.snrId.eq(snrId))
                .execute();
    }


}

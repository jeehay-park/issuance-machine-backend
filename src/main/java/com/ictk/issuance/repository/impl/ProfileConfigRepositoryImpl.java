package com.ictk.issuance.repository.impl;

import com.ictk.issuance.data.model.ProfileConfig;
import com.ictk.issuance.repository.dao.ProfileConfigDao;
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

import static com.ictk.issuance.data.model.QProfileConfig.profileConfig;


@Slf4j
@RequiredArgsConstructor
public class ProfileConfigRepositoryImpl extends IssuanceDaoImpl implements ProfileConfigDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean makeTable(String database, String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append("CREATE TABLE IF NOT EXISTS `"+tableName+"` ( \n");
        sbSQL.append(" `seq` int(11) NOT NULL COMMENT '순번 1부터 시작', \n");
        sbSQL.append(" `prof_id` varchar(32) NOT NULL COMMENT '프로파일 ID. prof_ + seq 의 형식', \n");
        sbSQL.append(" `prof_name` varchar(256) NOT NULL COMMENT '프로파일 이름', \n");
        sbSQL.append(" `description` varchar(256) DEFAULT NULL COMMENT '프로파일 상세 설명', \n");
        sbSQL.append(" `prof_type` varchar(32) DEFAULT NULL COMMENT '프로파일 타입(필요시)', \n");
        sbSQL.append(" `version` varchar(32) DEFAULT NULL COMMENT '버전 ex: 2.05', \n");
        sbSQL.append(" `ctnt_data` text NOT NULL COMMENT '프로파일 컨텐츠 데이터', \n");
        sbSQL.append(" `data_hash` varchar(64) DEFAULT NULL COMMENT '프로파일 데이터 해시', \n");
        sbSQL.append(" `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간', \n");
        sbSQL.append(" `created_at` datetime NOT NULL COMMENT '등록 시간', \n");
        sbSQL.append(" `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("        PRIMARY KEY (`prof_id`), \n");
        sbSQL.append("        UNIQUE KEY `IDX_PROFILE_CONFIG_01_UK` (`seq`) \n");
        sbSQL.append(" ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");


        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);

    }

    // 발급 프로파일 조회 (페이징)
    @Override
    public Tuple2<Long, Page<ProfileConfig>> getProfileConfigPageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers) {

        // total count 구하기
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(profileConfig.count())
                .from(profileConfig)
                .where(queryConds);

        Long total = countQuery.fetchCount();

        return Tuple.of(
                total,
                new PageImpl<>(
                        jpaQueryFactory.selectFrom(profileConfig)
                                .where(queryConds)
                                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]))
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch(),
                        pageable,
                        total ));


    }

    //
    @Override
    @Transactional
    public long deleteProfileConfigByProfId(String profId) {

        return jpaQueryFactory
                .delete(profileConfig)
                .where(profileConfig.profId.eq(profId))
                .execute();

    }

}

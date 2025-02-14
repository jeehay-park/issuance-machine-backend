package com.ictk.issuance.repository.impl;

import com.ictk.issuance.data.dto.codeenum.CodeEnumDTO;
import com.ictk.issuance.data.model.CodeEnum;
import com.ictk.issuance.repository.dao.CodeEnumDao;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
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

import static com.ictk.issuance.data.model.QCodeEnum.codeEnum;

@Slf4j
@RequiredArgsConstructor
public class CodeEnumRepositoryImpl extends IssuanceDaoImpl implements CodeEnumDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean makeTable(String database, String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append("CREATE TABLE IF NOT EXISTS `" + tableName + "` ( \n");
        sbSQL.append(" `seq` int(16) NOT NULL COMMENT '순번 1부터 시작',  \n");
        sbSQL.append(" `user_id` varchar(256) NOT NULL COMMENT '관리 사용자 ID. 'user_' + seq의 형식', \n");
        sbSQL.append(" `pass_salt` varchar(512) NOT NULL COMMENT '관리 사용자 비밀번호 salt', \n");
        sbSQL.append(" `password_hash` varchar(512) NOT NULL COMMENT '관리 사용자 비밀번호 hash', \n");
        sbSQL.append(" `name` varchar(256) NOT NULL COMMENT '관리 사용자 이름', \n");
        sbSQL.append(" `email` varchar(256) NOT NULL COMMENT '관리 사용자 이메일', \n");
        sbSQL.append(" `status` varchar(128) NOT NULL COMMENT '관리 사용자 상태', \n");
        sbSQL.append(" `updated_at` datetime NOT NULL COMMENT '업데이트 시간', \n");
        sbSQL.append(" `created_at` datetime NOT NULL COMMENT '등록 시간', \n");
        sbSQL.append(" `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("  PRIMARY KEY (`user_id`), \n");
        sbSQL.append("  UNIQUE KEY `IDX_USER_UK` (`seq`) \n");
        sbSQL.append(" ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");

        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }


    // 코드 ENUM 조회 (페이징)
    // a typical implementation for fetching a paged list of Enum entities
    // using QueryDSL with pagination and sorting.
    // It also calculates the total count of matching records for pagination
    public Tuple2<Long, Page<CodeEnum>> getEnumPageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers) {

        // total count 구하기
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(codeEnum.count())
                .from(codeEnum)
                .where(queryConds);

        Long total = countQuery.fetchCount();

        return Tuple.of(
                total,
                new PageImpl<>(
                        jpaQueryFactory.selectFrom(codeEnum)
                                .where(queryConds)
                                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]))
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch(),
                        pageable,
                        total));

    }

    // codeId로 삭제
    @Transactional
    @Override
    public long deleteCodeEnumByCodeId(String codeId) {
        return jpaQueryFactory
                .delete(codeEnum)
                .where(codeEnum.codeInfo.codeId.eq(codeId)) // Corrected path to codeId
                .execute();
    }

    // codeId로 조회
    @Transactional(readOnly = true)
    @Override
    public List<CodeEnumDTO.CodeEnumObj> findAllByCodeId(String codeId) {
        return jpaQueryFactory
                .select(Projections.constructor(CodeEnumDTO.CodeEnumObj.class,
                        codeEnum.enumValue,       // Maps to enumValue in CodeEnumObj
                        codeEnum.isMandatory,     // Maps to isMandatory in CodeEnumObj
                        codeEnum.ip,              // Maps to ip in CodeEnumObj
                        codeEnum.order,           // Maps to order in CodeEnumObj
                        codeEnum.description      // Maps to description in CodeEnumObj
                ))
                .from(codeEnum)
                .where(codeEnum.codeInfo.codeId.eq(codeId))
                .fetch();

    }
}

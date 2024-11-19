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

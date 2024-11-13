package com.ictk.issuance.repository.impl;

import com.ictk.issuance.data.model.CodeInfo;
import com.ictk.issuance.repository.dao.CodeInfoDao;
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

import static com.ictk.issuance.data.model.QCodeInfo.codeInfo;

@Slf4j
@RequiredArgsConstructor
public class CodeInfoRepositoryImpl extends IssuanceDaoImpl implements CodeInfoDao {


    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    // 코드 정보 테이블 생성하기
    @Override
    public boolean makeTable(String database, String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append(" CREATE TABLE IF NOT EXISTS `"+tableName+"` ( \n");
        sbSQL.append("  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작', \n");
        sbSQL.append("  `code_id` varchar(32) NOT NULL COMMENT '코드 ID. cd_ + seq의 형식', \n");
        sbSQL.append("  `code_name` varchar(64) NOT NULL COMMENT '코드 이름', \n");
        sbSQL.append("  `code_group` varchar(32) DEFAULT NULL COMMENT '코드 그룹(필요 시)', \n");
        sbSQL.append("  `description` varchar(255) DEFAULT NULL COMMENT '코드에 대한 상세설명', \n");
        sbSQL.append("  `status` varchar(32) NOT NULL COMMENT '코드 상태. USE/NOTUSE/DELETED', \n");
        sbSQL.append("  `updated_at` datetime NOT NULL COMMENT '업데이트 시간', \n");
        sbSQL.append("  `created_at` datetime NOT NULL COMMENT '등록 시간', \n");
        sbSQL.append("  `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("  PRIMARY KEY (`code_id`), \n");
        sbSQL.append("  UNIQUE KEY `IDX_CODE_INFO_UK` (`seq`) \n");
        sbSQL.append("  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");

        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }

    @Override
    // 코드 정보 조회 (페이징)
    public Tuple2<Long, Page<CodeInfo>> getCodeInfoPageByCondition(Predicate queryConds, Pageable pageable, List<OrderSpecifier> orderSpecifiers) {
        // total count 구하기
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(codeInfo.count())
                .from(codeInfo)
                .where(queryConds);

        Long total = countQuery.fetchCount();

        return Tuple.of(
                total,
                new PageImpl<>(
                        jpaQueryFactory.selectFrom(codeInfo)
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
    // 코드 정보 삭제
    public long deleteCodeInfoByCodeId(String codeId) {
        return jpaQueryFactory
                .delete(codeInfo)
                .where(codeInfo.codeId.eq(codeId))
                .execute();
    }


}

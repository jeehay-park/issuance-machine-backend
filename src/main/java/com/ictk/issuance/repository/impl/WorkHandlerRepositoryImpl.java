package com.ictk.issuance.repository.impl;

import static com.ictk.issuance.data.model.QProgramInfo.programInfo;
import static com.ictk.issuance.data.model.QWorkHandler.workHandler;

import com.ictk.issuance.data.model.WorkHandler;
import com.ictk.issuance.repository.dao.WorkHandlerDao;
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
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
public class WorkHandlerRepositoryImpl extends IssuanceDaoImpl implements WorkHandlerDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean makeTable(String database, String tableName) {

        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append(" CREATE TABLE IF NOT EXISTS `" + tableName + "` ( \n");
        sbSQL.append("  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작', \n");
        sbSQL.append("  `hdl_id` varchar(32) NOT NULL COMMENT '발급작업 핸들러 ID. hdl_ + seq의 형식', \n");
        sbSQL.append("  `work_id` varchar(32) NOT NULL COMMENT '발급작업 ID. wrk_ + seq의 형식', \n");
        sbSQL.append("  `dvc_id` varchar(32) NOT NULL COMMENT '발급기 디바이스 고유 ID. dvc_ + seq의 형식', \n");
        sbSQL.append("  `mcn_id` varchar(32) NOT NULL COMMENT '발급기 고유 ID. mcn_ + seq의 형식', \n");
        sbSQL.append("  `hdl_name` varchar(128) DEFAULT NULL COMMENT '작업 핸들러 이름', \n");
        sbSQL.append("  `status` varchar(32) DEFAULT NULL COMMENT '작업 핸들러 상태', \n");
        sbSQL.append("  `detail_msg` text DEFAULT NULL COMMENT '상세 메시지', \n");
        sbSQL.append("  `updated_at` datetime NOT NULL COMMENT '작업 업데이트 시간', \n");
        sbSQL.append("  `created_at` datetime NOT NULL COMMENT '작업 업데이트 시간', \n");
        sbSQL.append("  `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("  PRIMARY KEY (`hdl_id`), \n");
        sbSQL.append("  UNIQUE KEY `IDX_SN_RULE_01_UK` (`seq`) \n");
        sbSQL.append("  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");

        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }

    @Override
    @Transactional
    public long deleteWorkHandlerId(String hdlId) {
        return jpaQueryFactory
                .delete(workHandler)
                .where(workHandler.hdlId.eq(hdlId))
                .execute();
    }

    @Override
    public Optional<WorkHandler> findWorkHandlerByWorkId(String workId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(workHandler)
                        .where(workHandler.workId.eq(workId))
                        .fetchOne()
        );
    }

    @Override
    public Tuple2<Long, Page<WorkHandler>> getWorkHandlerPageByCondition(Predicate queryConds, Pageable pageable, List<OrderSpecifier> orderSpecifiers) {
        // total count 구하기

        JPAQuery<Long> countQuery
                = jpaQueryFactory
                .select(workHandler.count())
                .from(workHandler)
                .where(queryConds);

        Long total = countQuery.fetchCount();

        return Tuple.of(
                total,
                new PageImpl<>(
                        jpaQueryFactory.selectFrom(workHandler)
                                .where(queryConds)
                                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]))
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch(),
                        pageable,
                        total ));
    }
}
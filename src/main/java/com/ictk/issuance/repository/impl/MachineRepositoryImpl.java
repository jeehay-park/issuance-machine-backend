package com.ictk.issuance.repository.impl;

import com.ictk.issuance.data.model.Machine;
import com.ictk.issuance.repository.dao.MachineDao;
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

import java.time.LocalDateTime;
import java.util.List;

import static com.ictk.issuance.data.model.QMachine.machine;

@Slf4j
@RequiredArgsConstructor
public class MachineRepositoryImpl extends IssuanceDaoImpl implements MachineDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean makeTable(String database, String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append("CREATE TABLE IF NOT EXISTS `"+tableName+"` ( \n");
        sbSQL.append(" `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',  \n");
        sbSQL.append(" `mcn_id` varchar(32) NOT NULL COMMENT '발급기 고유 ID. mcn_ + seq의 형식', \n");
        sbSQL.append(" `mcn_name` varchar(64) DEFAULT NULL COMMENT '발급기 이름', \n");
        sbSQL.append(" `etc` text DEFAULT NULL COMMENT '기타 정보', \n");
        sbSQL.append(" `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간', \n");
        sbSQL.append(" `created_at` datetime NOT NULL COMMENT '등록 시간', \n");
        sbSQL.append(" `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("PRIMARY KEY (`mcn_id`), \n");
        sbSQL.append("UNIQUE KEY `IDX_MACHINE_UK` (`seq`) \n");
        sbSQL.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");

        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }

    //
    @Override
    @Transactional
    public long updateEtc(String mcnId, String etc) {
        return jpaQueryFactory
                .update(machine)
                .set(machine.etc, etc)
                .set(machine.updatedAt, LocalDateTime.now())
                .where(machine.mcnId.eq(mcnId))
                .execute();
    }

    // 머신 조회 (페이징)
    @Override
    public Tuple2<Long, Page<Machine>> getMachinePageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers) {

        // total count 구하기
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(machine.count())
                .from(machine)
                .where(queryConds);

        Long total = countQuery.fetchCount();

        return Tuple.of(
                    total,
                    new PageImpl<>(
                            jpaQueryFactory.selectFrom(machine)
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
    public long deleteMachineByMcnId(String mcnId) {

        return jpaQueryFactory
                .delete(machine)
                .where(machine.mcnId.eq(mcnId))
                .execute();

    }

    // 발급 기계 Id 목록
    @Override
    public List<String> findAllMcnIds() {
        return jpaQueryFactory
                .select(machine.mcnId)
                .from(machine)
                .fetch();
    }

}

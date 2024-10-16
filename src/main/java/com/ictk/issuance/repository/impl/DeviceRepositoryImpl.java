package com.ictk.issuance.repository.impl;

import com.ictk.issuance.data.model.Device;
import com.ictk.issuance.repository.dao.DeviceDao;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.ictk.issuance.data.model.QDevice.device;

@Slf4j
@RequiredArgsConstructor
public class DeviceRepositoryImpl extends IssuanceDaoImpl implements DeviceDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean makeTable(String database, String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append("CREATE TABLE IF NOT EXISTS `"+tableName+"` ( \n");

        sbSQL.append(" `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',  \n");
        sbSQL.append(" `dvc_id` varchar(32) NOT NULL COMMENT '발급기 디바이스 고유 ID. dvc_ + seq의 형식', \n");
        sbSQL.append(" `dvc_name` varchar(64) DEFAULT NULL COMMENT '발급기 디바이스 이름', \n");
        sbSQL.append(" `dvc_num` int(8) NOT NULL COMMENT '디바이스 번호. 발급기 기준으로 1번부터 할당', \n");
        sbSQL.append(" `mcn_id` varchar(32) NOT NULL COMMENT '발급기 고유 ID. mcn_ + seq의 형식', \n");
        sbSQL.append(" `ip` varchar(32) DEFAULT NULL COMMENT '디바이스 IP 주소', \n");
        sbSQL.append(" `rom_ver` varchar(32) DEFAULT NULL COMMENT '디바이스 롬(rom) 버전', \n");
        sbSQL.append(" `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간', \n");
        sbSQL.append(" `created_at` datetime NOT NULL COMMENT '등록 시간', \n");
        sbSQL.append(" `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("PRIMARY KEY (`dvc_id`), \n");
        sbSQL.append("UNIQUE KEY `IDX_MACHINE_DEVICE_01_UK` (`seq`) \n");
        sbSQL.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");

        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }

    // 발급기의 디바이스 dvcNum 데이터가 있는지 확인
    @Override
    public boolean existsByMcnIdAndDvcNum(String mcnId, int dvcNum) {
        return jpaQueryFactory
                .select(device.dvcNum)
                .from(device)
                .where(device.machine.mcnId.eq(mcnId).and( device.dvcNum.eq(dvcNum) ))
                .fetchCount() > 0;
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void saveDevice(Device dvc) {
//        if(dvc.getDvcId()!=null)
//            entityManager.merge(dvc);
//        else
//            entityManager.persist(dvc);
//    }

    // 디바이스 IP 변경
    @Override
    @Transactional
    public long updateIp(String dvcId, String ip) {
        return jpaQueryFactory
                .update(device)
                .set(device.ip, ip)
                .set(device.updatedAt, LocalDateTime.now())
                .where(device.dvcId.eq(dvcId))
                .execute();
    }

    // 디바이스 조회 (페이징)
    public Tuple2<Long, Page<Device>> getDevicePageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers) {

        // total count 구하기
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(device.count())
                .from(device)
                .where(queryConds);

        Long total = countQuery.fetchCount();

        return Tuple.of(
                total,
                new PageImpl<>(
                        jpaQueryFactory.selectFrom(device )
                                .where(queryConds)
                                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]))
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch(),
                        pageable,
                        total ));

    }

    // 디바이스(s) 삭제 (머신ID로 )
    @Override
    @Transactional
    public long deleteDevicesByMcnId(String mcnId) {
        return jpaQueryFactory
                .delete(device)
                .where( device.machine.mcnId.eq(mcnId) )
                .execute();
    }

}

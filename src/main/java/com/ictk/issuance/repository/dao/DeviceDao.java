package com.ictk.issuance.repository.dao;

import com.ictk.issuance.data.model.Device;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeviceDao {

    // 디바이스 테이블 생성하기
    boolean makeTable(String database, String tableName);

    // 발급기의 디바이스 dvcNum 데이터가 있는지 확인
    boolean existsByMcnIdAndDvcNum(String mcnId, int dvcNum);

    // void saveDevice(Device device);

    // 디바이스 IP 변경
    // @Transactional
    long updateIp(String dvcId, String ip);

    // 디바이스 조회 (페이징)
    Tuple2<Long, Page<Device>> getDevicePageByCondition(
            Predicate queryConds,
            Pageable pageable,
            List<OrderSpecifier> orderSpecifiers);

    // 디바이스(s) 삭제 (머신ID로 )
    // @Transactional
    long deleteDevicesByMcnId(String mcnId);

    // 디바이스 삭제
    // long deleteDeviceByDvcId(String dvcId);

}

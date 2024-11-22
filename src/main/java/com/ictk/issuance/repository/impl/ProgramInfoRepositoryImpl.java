package com.ictk.issuance.repository.impl;

import com.ictk.issuance.repository.dao.ProgramInfoDao;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ictk.issuance.data.model.QProgramInfo.programInfo;


@Slf4j
@RequiredArgsConstructor
public class ProgramInfoRepositoryImpl extends IssuanceDaoImpl implements ProgramInfoDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;


    @Override
    public boolean makeTable(String database, String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append("CREATE TABLE IF NOT EXISTS `" + tableName + "` ( \n");
        sbSQL.append(" `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',  \n");
        sbSQL.append(" `prog_id` varchar(32) NOT NULL COMMENT '발급 프로그램 고유 ID. 'prog_' + seq의 형식', \n");
        sbSQL.append(" `prog_name` varchar(64) DEFAULT NULL COMMENT '프로그램 이름', \n");
        sbSQL.append(" `product` varchar(32) DEFAULT NULL COMMENT '제품 (칩 Chip)', \n");
        sbSQL.append(" `test_code` varchar(64) DEFAULT NULL COMMENT '제품 테스트 코드', \n");
        sbSQL.append(" `description` text DEFAULT NULL COMMENT '프로그램 상세 설명', \n");
        sbSQL.append(" `status` varchar(16) NOT NULL COMMENT '프로그램 상태', \n");
        sbSQL.append(" `param` varchar(128) DEFAULT NULL COMMENT '파라미터', \n");
        sbSQL.append(" `param_ext` mediumtext DEFAULT NULL COMMENT '파라미터 확장', \n");
        sbSQL.append(" `is_encrypted_sn` varchar(16) NOT NULL COMMENT 'SN 인크립션 여부', \n");
        sbSQL.append(" `prof_id` varchar(32) NOT NULL COMMENT '프로파일 ID. 'prof_' + seq의 형식', \n");
        sbSQL.append(" `keyis_id` varchar(32) DEFAULT NULL COMMENT '키발급코드 ID. 'kis_' + seq의 형식', \n");
        sbSQL.append(" `scrt_id` varchar(32) DEFAULT NULL COMMENT '스크립트 ID. 'scrt_' + seq의 형식', \n");
        sbSQL.append(" `session_handler` varchar(128) DEFAULT NULL COMMENT '발급 핸들러 이름 (클래스 코드로 관리)', \n");
        sbSQL.append(" `etc_option` varchar(64) DEFAULT NULL COMMENT '기타 코드 옵션 명. 여러개인 경우 '|'로 구분', \n");
        sbSQL.append(" `company_code` varchar(32) DEFAULT NULL COMMENT '회사코드. ex GLOBAL LGU HAIER FINGER_CHIP LG_INNOTEK LGE 등', \n");
        sbSQL.append(" `country_code` varchar(32) DEFAULT NULL COMMENT '국가코드. ex : GLOBAL', \n");
        sbSQL.append(" `interface_type` varchar(32) DEFAULT NULL COMMENT '인터페이스 타입. ex : I2C SPI ONE_WIRE e7816', \n");
        sbSQL.append(" `package_type` varchar(32) DEFAULT NULL COMMENT '패키지 타입. ex : SOIC uDFN SOT_23 e7816', \n");
        sbSQL.append(" `updated_at` datetime NOT NULL COMMENT '업데이트 시간', \n");
        sbSQL.append(" `created_at` datetime NOT NULL COMMENT '등록 시간', \n");
        sbSQL.append(" `comment` text DEFAULT NULL COMMENT '주석 기타정보', \n");
        sbSQL.append("        PRIMARY KEY (`prog_id`), \n");
        sbSQL.append("        UNIQUE KEY `IDX_PROGRAM_INFO_UK` (`seq`) \n");
        sbSQL.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci; \n");

        log.info(sbSQL.toString());
        entityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(database, tableName);
    }

    @Override
    public long deleteProgramProgId(String progId) {
        return jpaQueryFactory
                .delete(programInfo)
                .where(programInfo.progId.eq(progId)).execute();
    }

}

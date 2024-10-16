package com.ictk.issuance.service.config;

import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.config.ConfigListDTO.ConfigListRQB;
import com.ictk.issuance.data.dto.config.ConfigListDTO.ConfigListRSB;
import com.ictk.issuance.data.dto.config.ConfigSearchDTO.ConfigSearchRQB;
import com.ictk.issuance.data.dto.config.ConfigSearchDTO.ConfigSearchRSB;
import com.ictk.issuance.data.dto.config.ConfigSaveDTO.ConfigSaveRQB;
import com.ictk.issuance.data.dto.config.ConfigSaveDTO.ConfigSaveRSB;
import com.ictk.issuance.data.dto.config.ConfigDeleteDTO.ConfigDeleteRQB;
import com.ictk.issuance.data.dto.config.ConfigDeleteDTO.ConfigDeleteRSB;


public interface ConfigService {

    // PROFILE_CONFIG 테이블 생성하기
    String createProfileConfigTable();

    // KEYISSUE_CONFIG 테이블 생성하기
    String createKeyissueConfigTable();

    // SCRIPT_CONFIG 테이블 생성하기
    String createScriptConfigTable();

    // 발급설정(프로파일/키발급코드/스크립트) 목록 조회 서비스
    ConfigListRSB queryConfigs(String trId, ConfigListRQB configListRQB) throws IctkException;

    // 발급설정(프로파일/키발급코드/스크립트) 정보 조회 서비스
    ConfigSearchRSB infoConfig(String trId, ConfigSearchRQB configRQB) throws IctkException;

    // 발급설정(프로파일/키발급코드/스크립트) 추가(생성)/변경(편집) 서비스
    ConfigSaveRSB saveConfig(String trId, ConfigSaveRQB saveRQB) throws IctkException;

    // 발급설정(프로파일/키발급코드/스크립트) 삭제 서비스
    ConfigDeleteRSB deleteConfig(String trId, ConfigDeleteRQB deleteRQB) throws IctkException;


}

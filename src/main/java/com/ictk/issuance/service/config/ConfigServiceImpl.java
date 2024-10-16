package com.ictk.issuance.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.constants.IssuanceConstants;
import com.ictk.issuance.data.dto.config.ConfigDTO.ProfileConfigObj;
import com.ictk.issuance.data.dto.config.ConfigDTO.KeyissueConfigObj;
import com.ictk.issuance.data.dto.config.ConfigDTO.ScriptConfigObj;
import com.ictk.issuance.data.dto.config.ConfigListDTO.ConfigListRQB;
import com.ictk.issuance.data.dto.config.ConfigListDTO.ConfigListRSB;
import com.ictk.issuance.data.dto.config.ConfigSaveDTO.ConfigSaveRQB;
import com.ictk.issuance.data.dto.config.ConfigSaveDTO.ConfigSaveRSB;
import com.ictk.issuance.data.dto.config.ConfigSearchDTO.ConfigSearchRQB;
import com.ictk.issuance.data.dto.config.ConfigSearchDTO.ConfigSearchRSB;
import com.ictk.issuance.data.dto.config.ConfigDeleteDTO.ConfigDeleteRQB;
import com.ictk.issuance.data.dto.config.ConfigDeleteDTO.ConfigDeleteRSB;
import com.ictk.issuance.data.dto.machine.MachineDeleteDTO;
import com.ictk.issuance.data.dto.shared.AppDTO;
import com.ictk.issuance.data.model.KeyissueConfig;
import com.ictk.issuance.data.model.ProfileConfig;
import com.ictk.issuance.data.model.ScriptConfig;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.ConfigProperties;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.repository.KeyissueConfigRepository;
import com.ictk.issuance.repository.ProfileConfigRepository;
import com.ictk.issuance.repository.ScriptConfigRepository;
import com.ictk.issuance.utils.AppHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ictk.issuance.data.model.QKeyissueConfig.keyissueConfig;
import static com.ictk.issuance.data.model.QProfileConfig.profileConfig;
import static com.ictk.issuance.data.model.QScriptConfig.scriptConfig;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConfigServiceImpl implements ConfigService {

    private final IssuanceManager issuanceManager;

    private final DBProperties dbProperties;

    private final ConfigProperties configProperties;

    private final ProfileConfigRepository profileConfigRepository;
    private final KeyissueConfigRepository keyissueConfigRepository;
    private final ScriptConfigRepository scriptConfigRepository;

    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    // PROFILE_CONFIG 테이블 생성하기
    public String createProfileConfigTable() {
        return issuanceManager.createTable( dbProperties.database(), configProperties.profile().tableName(),
                (database, table) -> {
                    if ( !profileConfigRepository.isTableExist(database, table) ) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if ( !profileConfigRepository.makeTable(database, table)) {
                            log.error("***** "+table+" 테이블 생성에 실패했습니다.");
                            return "FAIL";
                        } else
                            return "SUCC";
                    } else {
                        log.info("----- " + table + " 테이블이 존재합니다. ");
                        return "EXIST";
                    }
                });
    }

    @Override
    @Transactional
    // KEYISSUE_CONFIG 테이블 생성하기
    public String createKeyissueConfigTable() {
        return issuanceManager.createTable( dbProperties.database(), configProperties.keyissue().tableName(),
                (database, table) -> {
                    if ( !keyissueConfigRepository.isTableExist(database, table) ) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if ( !keyissueConfigRepository.makeTable(database, table)) {
                            log.error("***** "+table+" 테이블 생성에 실패했습니다.");
                            return "FAIL";
                        } else
                            return "SUCC";
                    } else {
                        log.info("----- " + table + " 테이블이 존재합니다. ");
                        return "EXIST";
                    }
                });
    }

    @Override
    @Transactional
    // SCRIPT_CONFIG 테이블 생성하기
    public String createScriptConfigTable() {
        return issuanceManager.createTable( dbProperties.database(), configProperties.script().tableName(),
                (database, table) -> {
                    if ( !scriptConfigRepository.isTableExist(database, table) ) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if ( !scriptConfigRepository.makeTable(database, table)) {
                            log.error("***** "+table+" 테이블 생성에 실패했습니다.");
                            return "FAIL";
                        } else
                            return "SUCC";
                    } else {
                        log.info("----- " + table + " 테이블이 존재합니다. ");
                        return "EXIST";
                    }
                });
    }

    // 발급설정(프로파일/키발급코드/스크립트) 목록 조회 서비스
    @Override
    public ConfigListRSB queryConfigs(String trId, ConfigListRQB configListRQB) throws IctkException {

        // 데이터 헤더 정보 구성
        Map<String, AppDTO.HeaderInfoObj> hdrInfoMap = AppHelper.configHeaderInfoMap( configListRQB.getConfigType() );
        final List<AppDTO.HeaderInfoObj> headerInfos = new ArrayList<AppDTO.HeaderInfoObj>();
        hdrInfoMap.forEach( (k,v) -> { headerInfos.add(v); } );

        // filter가 있는 경우에 기존 filterArrAndOr, filterArr는 무시되고 filter값이 우선시 된다.
        if(CommonUtils.hasValue(configListRQB.getFilter())) {
            var applied = AppHelper.applyFilterToRequestRQB( hdrInfoMap, configListRQB.getFilter() );
            configListRQB.setFilterArrAndOr(applied._1());
            configListRQB.setFilterArr(applied._2());
            log.debug("Filter checked! {} {}", configListRQB.getFilterArrAndOr(), CommonUtils.toJson(objectMapper, configListRQB.getFilterArr()));
        }

        // 동적 소팅 생성
        List<OrderSpecifier> orderSpecifiers = AppHelper.getRequestRQBOrderSpecifiers(
                getConfigClass(configListRQB.getConfigType()), getConfigClassName(configListRQB.getConfigType()),
                hdrInfoMap, configListRQB.getSortKeyName(), configListRQB.getOrder() );

        // 동적 쿼리 생성
        BooleanBuilder queryConds = new BooleanBuilder();
        if(CommonUtils.hasValue(configListRQB.getName())) {
            switch(configListRQB.getConfigType()) {
                case IssuanceConstants.CONFIG_TYPE_PROFILE -> { queryConds.and(profileConfig.profName.contains( configListRQB.getName() )); }
                case IssuanceConstants.CONFIG_TYPE_KEYISSUE -> { queryConds.and(keyissueConfig.keyisName.contains( configListRQB.getName() )); }
                case IssuanceConstants.CONFIG_TYPE_SCRIPT -> { queryConds.and(scriptConfig.scrtName.contains( configListRQB.getName() )); }
            }
        }
        BooleanBuilder filterConds = AppHelper.composeFilterDynamicConditions(
                configListRQB.getFilterArrAndOr(),
                configListRQB.getFilterArr(),
                (kname) -> {
                    switch(configListRQB.getConfigType()) {
                        case IssuanceConstants.CONFIG_TYPE_PROFILE -> {
                            switch(kname) {
                                case "prof_name" -> { return profileConfig.profName; }
                                case "version" -> { return profileConfig.version; }
                            }
                        }
                        case IssuanceConstants.CONFIG_TYPE_KEYISSUE -> {
                            switch(kname) {
                                case "keyis_name" -> { return keyissueConfig.keyisName; }
                                case "version" -> { return keyissueConfig.version; }
                            }
                        }
                        case IssuanceConstants.CONFIG_TYPE_SCRIPT -> {
                            switch(kname) {
                                case "scrt_name" -> { return scriptConfig.scrtName; }
                                case "version" -> { return scriptConfig.version; }
                            }
                        }
                    }
                    return null;
                } );

        if(filterConds!=null)
            queryConds.and(filterConds);


        switch(configListRQB.getConfigType()) {
            case IssuanceConstants.CONFIG_TYPE_PROFILE -> {
                // DB profile_config 테이블 쿼리
                Tuple2<Long, Page<ProfileConfig>> profileConfigsPaged = profileConfigRepository.getProfileConfigPageByCondition(
                        queryConds, configListRQB.getPageble(), orderSpecifiers);
                return ConfigListRSB.builder()
                        .totalCnt( profileConfigsPaged._1() )
                        .curPage( configListRQB.getPageble().getPageNumber() )
                        .headerInfos( configListRQB.isHeaderInfo() ? headerInfos : null )
                        .configList( composeProfileConfigList(hdrInfoMap, profileConfigsPaged._2().toList()) )
                        .build();
            }
            case IssuanceConstants.CONFIG_TYPE_KEYISSUE -> {
                // DB keyissue_config 테이블 쿼리
                Tuple2<Long, Page<KeyissueConfig>> keyissueConfigsPaged = keyissueConfigRepository.getKeyissueConfigPageByCondition(
                        queryConds, configListRQB.getPageble(), orderSpecifiers);
                return ConfigListRSB.builder()
                        .totalCnt( keyissueConfigsPaged._1() )
                        .curPage( configListRQB.getPageble().getPageNumber() )
                        .headerInfos( configListRQB.isHeaderInfo() ? headerInfos : null )
                        .configList( composeKeyissueConfigList(hdrInfoMap, keyissueConfigsPaged._2().toList()) )
                        .build();
            }
            case IssuanceConstants.CONFIG_TYPE_SCRIPT -> {
                // DB script_config 테이블 쿼리
                Tuple2<Long, Page<ScriptConfig>> scriptConfigsPaged = scriptConfigRepository.getScriptConfigPageByCondition(
                        queryConds, configListRQB.getPageble(), orderSpecifiers);
                return ConfigListRSB.builder()
                        .totalCnt(scriptConfigsPaged._1() )
                        .curPage( configListRQB.getPageble().getPageNumber() )
                        .headerInfos( configListRQB.isHeaderInfo() ? headerInfos : null )
                        .configList( composeScriptConfigList(hdrInfoMap, scriptConfigsPaged._2().toList()) )
                        .build();
            }
        }

        throw new IctkException(trId, AppCode.ISSUANCE_CONFIG_ERROR, "발급 설정 "+configListRQB.getConfigType()+ " 목록 조회.");

    }

    private Class<?> getConfigClass(String configType) {
        switch(configType) {
            case IssuanceConstants.CONFIG_TYPE_PROFILE -> { return ProfileConfig.class; }
            case IssuanceConstants.CONFIG_TYPE_KEYISSUE -> { return KeyissueConfig.class; }
            case IssuanceConstants.CONFIG_TYPE_SCRIPT -> { return ScriptConfig.class; }
        }
        return null;
    }

    private String getConfigClassName(String configType) {
        switch(configType) {
            case IssuanceConstants.CONFIG_TYPE_PROFILE -> { return "profileConfig"; }
            case IssuanceConstants.CONFIG_TYPE_KEYISSUE -> { return "keyissueConfig"; }
            case IssuanceConstants.CONFIG_TYPE_SCRIPT -> { return "scriptConfig"; }
        }
        return null;
    }

    private List<Map<String,Object>> composeProfileConfigList(Map<String, AppDTO.HeaderInfoObj> hdrInfoMap, List<ProfileConfig> profConfigs) {
        final List<Map<String,Object>> bodyList = new ArrayList<>();
        if(hdrInfoMap!=null && profConfigs!=null) {
            AtomicInteger idx = new AtomicInteger(1);
            profConfigs.forEach( profConfig -> {
                Map<String,Object> dataMap = new LinkedHashMap<>();
                dataMap.put("idx", idx.get() );
                hdrInfoMap.forEach( (k,v) -> {
                    switch(v.getKeyName()) {
                        case "prof_id" -> dataMap.put(v.getKeyName(), profConfig.getProfId());
                        case "prof_name" -> dataMap.put(v.getKeyName(), profConfig.getProfName());
                        case "description" -> dataMap.put(v.getKeyName(), profConfig.getDescription());
                        case "prof_type" -> dataMap.put(v.getKeyName(), profConfig.getProfType());
                        case "version" -> dataMap.put(v.getKeyName(), profConfig.getVersion());
                        case "data_hash" -> dataMap.put(v.getKeyName(), profConfig.getDataHash());
                        case "updated_at" -> dataMap.put(v.getKeyName(), profConfig.getUpdatedAt()
                                .format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ));
                        case "created_at" -> dataMap.put(v.getKeyName(), profConfig.getCreatedAt()
                                .format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ));
                        default -> {}
                    }
                });
                bodyList.add(dataMap);
                idx.getAndIncrement();
            } );
        }
        return bodyList;
    }

    private List<Map<String,Object>> composeKeyissueConfigList(Map<String, AppDTO.HeaderInfoObj> hdrInfoMap, List<KeyissueConfig> keyisConfigs) {
        final List<Map<String,Object>> bodyList = new ArrayList<>();
        if(hdrInfoMap!=null && keyisConfigs!=null) {
            AtomicInteger idx = new AtomicInteger(1);
            keyisConfigs.forEach( keyisConfig -> {
                Map<String,Object> dataMap = new LinkedHashMap<>();
                dataMap.put("idx", idx.get() );
                hdrInfoMap.forEach( (k,v) -> {
                    switch(v.getKeyName()) {
                        case "keyis_id" -> dataMap.put(v.getKeyName(), keyisConfig.getKeyisId());
                        case "keyis_name" -> dataMap.put(v.getKeyName(), keyisConfig.getKeyisName());
                        case "description" -> dataMap.put(v.getKeyName(), keyisConfig.getDescription());
                        case "keyis_type" -> dataMap.put(v.getKeyName(), keyisConfig.getKeyisType());
                        case "version" -> dataMap.put(v.getKeyName(), keyisConfig.getVersion());
                        case "data_hash" -> dataMap.put(v.getKeyName(), keyisConfig.getDataHash());
                        case "updated_at" -> dataMap.put(v.getKeyName(), keyisConfig.getUpdatedAt()
                                .format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ));
                        case "created_at" -> dataMap.put(v.getKeyName(), keyisConfig.getCreatedAt()
                                .format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ));
                        default -> {}
                    }
                });
                bodyList.add(dataMap);
                idx.getAndIncrement();
            } );
        }
        return bodyList;
    }

    private List<Map<String,Object>> composeScriptConfigList(Map<String, AppDTO.HeaderInfoObj> hdrInfoMap, List<ScriptConfig> scrtConfigs) {
        final List<Map<String,Object>> bodyList = new ArrayList<>();
        if(hdrInfoMap!=null && scrtConfigs!=null) {
            AtomicInteger idx = new AtomicInteger(1);
            scrtConfigs.forEach( scrtConfig -> {
                Map<String,Object> dataMap = new LinkedHashMap<>();
                dataMap.put("idx", idx.get() );
                hdrInfoMap.forEach( (k,v) -> {
                    switch(v.getKeyName()) {
                        case "scrt_id" -> dataMap.put(v.getKeyName(), scrtConfig.getScrtId());
                        case "scrt_name" -> dataMap.put(v.getKeyName(), scrtConfig.getScrtName());
                        case "description" -> dataMap.put(v.getKeyName(), scrtConfig.getDescription());
                        case "scrt_type" -> dataMap.put(v.getKeyName(), scrtConfig.getScrtType());
                        case "version" -> dataMap.put(v.getKeyName(), scrtConfig.getVersion());
                        case "data_hash" -> dataMap.put(v.getKeyName(), scrtConfig.getDataHash());
                        case "updated_at" -> dataMap.put(v.getKeyName(), scrtConfig.getUpdatedAt()
                                .format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ));
                        case "created_at" -> dataMap.put(v.getKeyName(), scrtConfig.getCreatedAt()
                                .format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ));
                        default -> {}
                    }
                });
                bodyList.add(dataMap);
                idx.getAndIncrement();
            } );
        }
        return bodyList;
    }

    // 발급설정(프로파일/키발급코드/스크립트) 정보 조회 서비스
    @Override
    public ConfigSearchRSB infoConfig(String trId, ConfigSearchRQB configRQB) throws IctkException {

        switch(configRQB.getConfigType()) {
            case IssuanceConstants.CONFIG_TYPE_PROFILE -> {
                return profileConfigRepository.findProfileConfigByProfId( configRQB.getProfId() )
                        .map( config -> ConfigSearchRSB.builder()
                                .configType(IssuanceConstants.CONFIG_TYPE_PROFILE )
                                .profileConfig( ProfileConfigObj.builder()
                                        .profId( config.getProfId() )
                                        .profName( config.getProfName())
                                        .description( config.getDescription())
                                        .profType( config.getProfType())
                                        .version( config.getVersion())
                                        .ctntData( config.getCtntData())
                                        .dataHash( config.getDataHash())
                                        .updatedAt( config.getUpdatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                                        .createdAt( config.getCreatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                                        .build() )
                                .build() )
                        .orElseThrow( () -> new IctkException(trId, AppCode.ISSUANCE_CONFIG_ERROR, "프로파일 "+configRQB.getProfId()+ " 없음.") );
            }
            case IssuanceConstants.CONFIG_TYPE_KEYISSUE -> {
                return keyissueConfigRepository.findKeyissueConfigByKeyisId( configRQB.getKeyisId() )
                        .map( config -> ConfigSearchRSB.builder()
                                .configType(IssuanceConstants.CONFIG_TYPE_KEYISSUE )
                                .keyissueConfig( KeyissueConfigObj.builder()
                                        .keyisId( config.getKeyisId() )
                                        .keyisName( config.getKeyisName())
                                        .description( config.getDescription())
                                        .keyisType( config.getKeyisType())
                                        .version( config.getVersion())
                                        .ctntData( config.getCtntData())
                                        .dataHash( config.getDataHash())
                                        .updatedAt( config.getUpdatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                                        .createdAt( config.getCreatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                                        .build() )
                                .build() )
                        .orElseThrow( () -> new IctkException(trId, AppCode.ISSUANCE_CONFIG_ERROR, "키발급코드 "+configRQB.getKeyisId()+ " 없음.") );
            }
            case IssuanceConstants.CONFIG_TYPE_SCRIPT -> {
                return scriptConfigRepository.findScriptConfigByScrtId( configRQB.getScrtId() )
                        .map( config -> ConfigSearchRSB.builder()
                                .configType(IssuanceConstants.CONFIG_TYPE_SCRIPT )
                                .scriptConfig( ScriptConfigObj.builder()
                                        .scrtId( config.getScrtId() )
                                        .scrtName( config.getScrtName())
                                        .description( config.getDescription())
                                        .scrtType( config.getScrtType())
                                        .version( config.getVersion())
                                        .ctntData( config.getCtntData())
                                        .dataHash( config.getDataHash())
                                        .updatedAt( config.getUpdatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                                        .createdAt( config.getCreatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                                        .build() )
                                .build() )
                        .orElseThrow( () -> new IctkException(trId, AppCode.ISSUANCE_CONFIG_ERROR, "스크립트 "+configRQB.getScrtId()+ " 없음.") );
            }
        }
        return null;
    }


    // 발급설정(프로파일/키발급코드/스크립트) 추가(생성)/변경(편집) 서비스
    @Override
    @Transactional
    public ConfigSaveRSB saveConfig(String trId, ConfigSaveRQB saveRQB) throws IctkException {
        boolean toUpdate = false;

        switch(saveRQB.getConfigType()) {
            case IssuanceConstants.CONFIG_TYPE_PROFILE -> {
                ProfileConfig findProfConfig = null;
                if(saveRQB.getProfileConfig()==null )
                    throw new IctkException(trId, AppCode.ISSUANCE_CONFIG_ERROR, "profileConfig 정보 없음.");
                if( CommonUtils.hasValue(saveRQB.getProfileConfig().getProfId() )) {
                    toUpdate = true;
                    findProfConfig = profileConfigRepository.findProfileConfigByProfId( saveRQB.getProfileConfig().getProfId() )
                            .orElseThrow( () -> new IctkException(trId, AppCode.MACHINE_PROC_ERROR,
                                    "프로파일 설정정보 " +saveRQB.getProfileConfig().getProfId()+ " 없음.") );
                    findProfConfig.setProfName( saveRQB.getProfileConfig().getProfName() );
                    findProfConfig.setDescription( saveRQB.getProfileConfig().getDescription() );
                    findProfConfig.setProfType( saveRQB.getProfileConfig().getProfType() );
                    findProfConfig.setVersion( saveRQB.getProfileConfig().getVersion() );
                    findProfConfig.setCtntData( saveRQB.getProfileConfig().getCtntData() );
                    findProfConfig.setDataHash( saveRQB.getProfileConfig().getDataHash() );
                    findProfConfig.setUpdatedAt( LocalDateTime.now());
                }
                ProfileConfig saveProfConfig = profileConfigRepository.save( toUpdate ? findProfConfig :
                        ProfileConfig.builder()
                                .profName( saveRQB.getProfileConfig().getProfName() )
                                .description( saveRQB.getProfileConfig().getDescription() )
                                .profType( saveRQB.getProfileConfig().getProfType() )
                                .version( saveRQB.getProfileConfig().getVersion() )
                                .ctntData( saveRQB.getProfileConfig().getCtntData() )
                                .dataHash( saveRQB.getProfileConfig().getDataHash() )
                                .createdAt( LocalDateTime.now() )
                                .build());

                return ConfigSaveRSB.builder()
                        .profId( saveProfConfig.getProfId() )
                        .profName(saveProfConfig.getProfName() )
                        .build();
            }
            case IssuanceConstants.CONFIG_TYPE_KEYISSUE -> {
                KeyissueConfig findKeyisConfig = null;
                if(saveRQB.getKeyissueConfig()==null )
                    throw new IctkException(trId, AppCode.ISSUANCE_CONFIG_ERROR, "keyissueConfig 정보 없음.");
                if( CommonUtils.hasValue(saveRQB.getKeyissueConfig().getKeyisId() )) {
                    toUpdate = true;
                    findKeyisConfig = keyissueConfigRepository.findKeyissueConfigByKeyisId( saveRQB.getKeyissueConfig().getKeyisId() )
                            .orElseThrow( () -> new IctkException(trId, AppCode.MACHINE_PROC_ERROR,
                                    "키발급코드 설정정보 " +saveRQB.getKeyissueConfig().getKeyisId()+ " 없음.") );
                    findKeyisConfig.setKeyisName( saveRQB.getKeyissueConfig().getKeyisName() );
                    findKeyisConfig.setDescription( saveRQB.getKeyissueConfig().getDescription() );
                    findKeyisConfig.setKeyisType( saveRQB.getKeyissueConfig().getKeyisType() );
                    findKeyisConfig.setVersion( saveRQB.getKeyissueConfig().getVersion() );
                    findKeyisConfig.setCtntData( saveRQB.getKeyissueConfig().getCtntData() );
                    findKeyisConfig.setDataHash( saveRQB.getKeyissueConfig().getDataHash() );
                    findKeyisConfig.setUpdatedAt( LocalDateTime.now());
                }
                KeyissueConfig saveKeyisConfig = keyissueConfigRepository.save( toUpdate ? findKeyisConfig :
                        KeyissueConfig.builder()
                                .keyisName( saveRQB.getKeyissueConfig().getKeyisName() )
                                .description( saveRQB.getKeyissueConfig().getDescription() )
                                .keyisType( saveRQB.getKeyissueConfig().getKeyisType() )
                                .version( saveRQB.getKeyissueConfig().getVersion() )
                                .ctntData( saveRQB.getKeyissueConfig().getCtntData() )
                                .dataHash( saveRQB.getKeyissueConfig().getDataHash() )
                                .createdAt( LocalDateTime.now())
                                .build());
                return ConfigSaveRSB.builder()
                        .keyisId( saveKeyisConfig.getKeyisId() )
                        .keyisName(saveKeyisConfig.getKeyisName() )
                        .build();
            }
            case IssuanceConstants.CONFIG_TYPE_SCRIPT -> {
                ScriptConfig findScrtConfig = null;
                if(saveRQB.getScriptConfig()==null )
                    throw new IctkException(trId, AppCode.ISSUANCE_CONFIG_ERROR, "scriptConfig 정보 없음.");
                if( CommonUtils.hasValue(saveRQB.getScriptConfig().getScrtId() )) {
                    toUpdate = true;
                    findScrtConfig = scriptConfigRepository.findScriptConfigByScrtId( saveRQB.getScriptConfig().getScrtId() )
                            .orElseThrow( () -> new IctkException(trId, AppCode.MACHINE_PROC_ERROR,
                                    "스크립트 설정정보 " +saveRQB.getScriptConfig().getScrtId()+ " 없음.") );
                    findScrtConfig.setScrtName( saveRQB.getScriptConfig().getScrtName() );
                    findScrtConfig.setDescription( saveRQB.getScriptConfig().getDescription() );
                    findScrtConfig.setScrtType( saveRQB.getScriptConfig().getScrtType() );
                    findScrtConfig.setVersion( saveRQB.getScriptConfig().getVersion() );
                    findScrtConfig.setCtntData( saveRQB.getScriptConfig().getCtntData() );
                    findScrtConfig.setDataHash( saveRQB.getScriptConfig().getDataHash() );
                    findScrtConfig.setUpdatedAt( LocalDateTime.now());
                }
                ScriptConfig saveScrtConfig = scriptConfigRepository.save( toUpdate ? findScrtConfig :
                        ScriptConfig.builder()
                                .scrtName( saveRQB.getScriptConfig().getScrtName() )
                                .description( saveRQB.getScriptConfig().getDescription() )
                                .scrtType( saveRQB.getScriptConfig().getScrtType() )
                                .version( saveRQB.getScriptConfig().getVersion() )
                                .ctntData( saveRQB.getScriptConfig().getCtntData() )
                                .dataHash( saveRQB.getScriptConfig().getDataHash() )
                                .createdAt( LocalDateTime.now())
                                .build());
                return ConfigSaveRSB.builder()
                        .keyisId( saveScrtConfig.getScrtId() )
                        .keyisName(saveScrtConfig.getScrtName() )
                        .build();
            }
        }
        return null;

    }


    // 발급설정(프로파일/키발급코드/스크립트) 삭제 서비스
    @Override
    public ConfigDeleteRSB deleteConfig(String trId, ConfigDeleteRQB deleteRQB) throws IctkException {

        switch(deleteRQB.getConfigType()) {
            case IssuanceConstants.CONFIG_TYPE_PROFILE -> {
                profileConfigRepository.findProfileConfigByProfId( deleteRQB.getProfId() )
                        .orElseThrow( () -> new IctkException(trId, AppCode.ISSUANCE_CONFIG_ERROR, "프로파일 "+deleteRQB.getProfId()+ " 없음.") );
                long dcnt = profileConfigRepository.deleteProfileConfigByProfId( deleteRQB.getProfId() );
                return ConfigDeleteRSB.builder()
                        .result( (dcnt>0) ? AppConstants.SUCC : AppConstants.FAIL )
                        .deleteCnt( (int)dcnt)
                        .build();
            }
            case IssuanceConstants.CONFIG_TYPE_KEYISSUE -> {
                keyissueConfigRepository.findKeyissueConfigByKeyisId( deleteRQB.getKeyisId() )
                        .orElseThrow( () -> new IctkException(trId, AppCode.ISSUANCE_CONFIG_ERROR, "키발급코드 "+deleteRQB.getKeyisId()+ " 없음.") );
                long dcnt = keyissueConfigRepository.deleteKeyissueConfigByKeyisId( deleteRQB.getKeyisId() );
                return ConfigDeleteRSB.builder()
                        .result( (dcnt>0) ? AppConstants.SUCC : AppConstants.FAIL )
                        .deleteCnt( (int)dcnt)
                        .build();
            }
            case IssuanceConstants.CONFIG_TYPE_SCRIPT -> {
                scriptConfigRepository.findScriptConfigByScrtId( deleteRQB.getScrtId() )
                        .orElseThrow( () -> new IctkException(trId, AppCode.ISSUANCE_CONFIG_ERROR, "스크립트 "+deleteRQB.getScrtId()+ " 없음.") );
                long dcnt = scriptConfigRepository.deleteScriptConfigByScrtId( deleteRQB.getScrtId() );
                return ConfigDeleteRSB.builder()
                        .result( (dcnt>0) ? AppConstants.SUCC : AppConstants.FAIL )
                        .deleteCnt( (int)dcnt)
                        .build();
            }
        }
        return null;
    }

}

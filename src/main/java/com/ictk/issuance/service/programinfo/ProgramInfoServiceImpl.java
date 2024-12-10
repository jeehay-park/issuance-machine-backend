package com.ictk.issuance.service.programinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.config.ConfigDTO;
import com.ictk.issuance.data.dto.programinfo.*;
import com.ictk.issuance.data.dto.shared.AppDTO;
import com.ictk.issuance.data.model.KeyissueConfig;
import com.ictk.issuance.data.model.ProfileConfig;
import com.ictk.issuance.data.model.ProgramInfo;
import com.ictk.issuance.data.model.ScriptConfig;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.properties.ProgramInfoProperties;
import com.ictk.issuance.repository.ProgramInfoRepository;
import com.ictk.issuance.utils.AppHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import io.jsonwebtoken.Header;
import io.vavr.Tuple2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ictk.issuance.data.model.QProgramInfo.programInfo;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProgramInfoServiceImpl implements ProgramInfoService {

    private final IssuanceManager issuanceManager;

    private final DBProperties dbProperties;

    private final ProgramInfoProperties programInfoProperties;

    private final ProgramInfoRepository programInfoRepository;

    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    // 프로그램 정보 테이블 생성하기
    public String createProgramInfoTable() {
        return issuanceManager.createTable(dbProperties.database(), programInfoProperties.tableName(),
                (database, table) -> {
                    if (!programInfoRepository.isTableExist(database, table)) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if (!programInfoRepository.makeTable(database, table)) {
                            log.info("----- " + table + " 테이블 생성에 실패했습니다. ");
                            return "FAIL";
                        } else {
                            return "SUCC";
                        }
                    } else {
                        log.info("----- " + table + " 테이블이 존재합니다. ");
                        return "EXIST";
                    }
                });
    }

    @Override
    @Transactional
    // 프로그램 단일 정보 조회 서비스
    public ProgramInfoSearchDTO.ProgramInfoSearchRSB searchProgram(String trId, ProgramInfoSearchDTO.ProgramInfoSearchRQB programInfoSearchRQB) throws IctkException {
        ProgramInfoSearchDTO.ProgramInfoSearchRSB programInfoSearchRSB = programInfoRepository
                .findById(programInfoSearchRQB.getProgId())
                .map(programInfo -> ProgramInfoSearchDTO.ProgramInfoSearchRSB.builder()
                        .progId(programInfo.getProgId())
                        .progName(programInfo.getProgName())
                        .description(programInfo.getDescription())
                        .product(programInfo.getProduct())
                        .sessionHandler(programInfo.getSessionHandler())
                        .testCode(programInfo.getTestCode())
                        .etcOption(
                                Arrays.asList(programInfo.getEtcOption().split("\\|"))
                        )
                        .profileInfo(getProfileConfigObjList(programInfo.getProfileConfig()))
                        .keyIssueInfo(getKeyissueConfigObjList(programInfo.getKeyIssueInfo()))
                        .scriptInfo(getScriptConfigObjList(programInfo.getScriptInfo()))
                        .isEncryptSn(programInfo.isEncryptedSn())
                        .companyCode(programInfo.getCompanyCode())
                        .countryCode(programInfo.getCountryCode())
                        .interfaceType(programInfo.getInterfaceType())
                        .packageType(programInfo.getPackageType())
                        .createdAt(programInfo.getCreatedAt().format(DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT)))
                        .build()
                )
                .orElseThrow(
                        ()
                        -> new IctkException(trId, AppCode.PROGRAM_PROC_ERROR, "발급기계 " + programInfoSearchRQB.getProgId() + " 없음."));

        return programInfoSearchRSB;
    }

    // ProfileConfig -> ProfileConfigObj
    private List<ConfigDTO.ProfileConfigObj> getProfileConfigObjList(List<ProfileConfig> profileConfig) {
        List<ConfigDTO.ProfileConfigObj> profileConfigObjList = new ArrayList<>();
        if (CommonUtils.hasElements(profileConfig)) {
            profileConfig.forEach(item -> {
                profileConfigObjList.add(ConfigDTO.ProfileConfigObj.builder()
                        .profId(item.getProfId())
                        .profName(item.getProfName())
                        .description(item.getDescription())
                        .build());
            });
        }
        return profileConfigObjList;
    }


    // KeyissueConfig -> KeyissueConfigObj
    private List<ConfigDTO.KeyissueConfigObj> getKeyissueConfigObjList(List<KeyissueConfig> keyissueConfig) {
        List<ConfigDTO.KeyissueConfigObj> keyissueConfigObjList = new ArrayList<>();
        if (CommonUtils.hasElements(keyissueConfig)) {
            keyissueConfig.forEach(item -> {
                keyissueConfigObjList.add(ConfigDTO.KeyissueConfigObj.builder()
                        .keyisId(item.getKeyisId())
                        .keyisName(item.getKeyisName())
                        .description(item.getDescription())
                        .build());
            });
        }
        return keyissueConfigObjList;
    }

    // ScriptConfig -> ScriptConfigObj
    private List<ConfigDTO.ScriptConfigObj> getScriptConfigObjList(List<ScriptConfig> scriptConfig) {
        List<ConfigDTO.ScriptConfigObj> scriptConfigObjList = new ArrayList<>();
        if (CommonUtils.hasElements(scriptConfig)) {
            scriptConfig.forEach(item -> {
                scriptConfigObjList.add(ConfigDTO.ScriptConfigObj.builder()
                        .scrtId(item.getScrtId())
                        .scrtName(item.getScrtName())
                        .description(item.getDescription())
                        .build());
            });
        }
        return scriptConfigObjList;
    }

    @Override
    // 프로그램 목록 조회 서비스
    public ProgramInfoListDTO.ProgramInfoListRSB fetchProgramList(String trId, ProgramInfoListDTO.ProgramInfoListRQB programInfoListRQB) throws IctkException {

        // 데이터 헤더 정보 구성
        Map<String, AppDTO.HeaderInfoObj> hdrInfoMap = AppHelper.programInfoHeaderInfoMap();
        final List<AppDTO.HeaderInfoObj> headerInfos = new ArrayList<AppDTO.HeaderInfoObj>();

        hdrInfoMap.forEach((key, value) -> {
            headerInfos.add(value);
        });

        // filter가 있는 경우에 기존 filterArrAndOr, filterArr는 무시되고 filter값이 우선시 된다.
        if (CommonUtils.hasValue(programInfoListRQB.getFilter())) {
            var applied = AppHelper.applyFilterToRequestRQB(hdrInfoMap, programInfoListRQB.getFilter());
            programInfoListRQB.setFilterArrAndOr((applied._1()));
            programInfoListRQB.setFilterArr(applied._2());
            log.debug("Filter checked! {} {}", programInfoListRQB.getFilterArrAndOr(), CommonUtils.toJson(objectMapper, programInfoListRQB.getFilterArr()));
        }

        // 동적 소팅 생성
        List<OrderSpecifier> orderSpecifiers = AppHelper.getRequestRQBOrderSpecifiers(
                ProgramInfo.class, "programInfo", hdrInfoMap, programInfoListRQB.getSortKeyName(), programInfoListRQB.getOrder()
        );

        // 동적 쿼리 생성
        BooleanBuilder queryConds = new BooleanBuilder();
        if (CommonUtils.hasValue(programInfoListRQB.getProgName()))
            queryConds.and(programInfo.progName.contains(programInfoListRQB.getProgName()));

        BooleanBuilder filterConds = AppHelper.composeFilterDynamicConditions(
                programInfoListRQB.getFilterArrAndOr(),
                programInfoListRQB.getFilterArr(),
                (keyName) -> {
                    switch (keyName) {
                        case "prog_name" -> {
                            return programInfo.progName;
                        }
                        case "product" -> {
                            return programInfo.product;
                        }
                    }
                    return null;
                });

        if (filterConds != null) {
            queryConds.and(filterConds);
        }

        StopWatch timer = new StopWatch();
        timer.start();

        // 테이블 쿼리
        Tuple2<Long, Page<ProgramInfo>> programInfoPaged = programInfoRepository.getProgramInfoPageByCondition(
                queryConds,
                programInfoListRQB.getPageable(),
                orderSpecifiers
        );

        timer.stop();
        log.info("Query took {} nanos ", (int) Math.round(timer.getTotalTimeNanos()));

        return ProgramInfoListDTO.ProgramInfoListRSB.builder()
                .totalCnt(programInfoPaged._1())
                .curPage(programInfoListRQB.getPageable().getPageNumber())
                .headerInfos(programInfoListRQB.isHeaderInfo() ? headerInfos : null)
                .programList(composeProgramList(hdrInfoMap, programInfoPaged._2().toList()))
                .build();
    }

    private List<Map<String, Object>> composeProgramList(Map<String, AppDTO.HeaderInfoObj> hdrInfoMap, List<ProgramInfo> programInfos) {
        final List<Map<String, Object>> bodyList = new ArrayList<>();
        if (hdrInfoMap != null && programInfos != null) {
            AtomicInteger idx = new AtomicInteger(1);
            programInfos.forEach(programInfo -> {
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("idx", idx.get());
                hdrInfoMap.forEach((key, value) -> {
                    switch (value.getKeyName()) {
                        case "prog_id" -> dataMap.put(value.getKeyName(), programInfo.getProgId());
                        case "prog_name" -> dataMap.put(value.getKeyName(), programInfo.getProgName());
                        case "product" -> dataMap.put(value.getKeyName(), programInfo.getProduct());
                        case "test_code" -> dataMap.put(value.getKeyName(), programInfo.getTestCode());
                        case "description" -> dataMap.put(value.getKeyName(), programInfo.getDescription());
                        case "status" -> dataMap.put(value.getKeyName(), programInfo.getStatus());
                        case "param" -> dataMap.put(value.getKeyName(), programInfo.getParam());
                        case "param_ext" -> dataMap.put(value.getKeyName(), programInfo.getParamExt());
                        case "is_encrypted_sn" -> dataMap.put(value.getKeyName(), programInfo.isEncryptedSn());
                        case "prof_id" -> dataMap.put(value.getKeyName(), programInfo.getProfId());
                        case "keyis_id" -> dataMap.put(value.getKeyName(), programInfo.getKeyisId());
                        case "scrt_id" -> dataMap.put(value.getKeyName(), programInfo.getScrtId());
                        case "session_handler" -> dataMap.put(value.getKeyName(), programInfo.getSessionHandler());
                        case "etc_option" -> dataMap.put(value.getKeyName(), programInfo.getEtcOption());
                        case "company_code" -> dataMap.put(value.getKeyName(), programInfo.getCompanyCode());
                        case "country_code" -> dataMap.put(value.getKeyName(), programInfo.getCountryCode());
                        case "interface_type" -> dataMap.put(value.getKeyName(), programInfo.getInterfaceType());
                        case "package_type" -> dataMap.put(value.getKeyName(), programInfo.getPackageType());
                        case "updated_at" ->
                                dataMap.put(value.getKeyName(), programInfo.getUpdatedAt().format(DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT)));
                        case "created_at" ->
                                dataMap.put(value.getKeyName(), programInfo.getCreatedAt().format(DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT)));
                        case "comment" -> dataMap.put(value.getKeyName(), programInfo.getComment());
                        default -> {
                        }
                    }
                });
                bodyList.add(dataMap);
                idx.getAndIncrement();
            });
        }
        return bodyList;
    }

    @Override
    @Transactional
    // 프로그램 생성/변경 서비스
    public ProgramInfoSaveDTO.ProgramInfoSaveRSB saveProgram(String trId, ProgramInfoSaveDTO.ProgramInfoSaveRQB programInfoSaveRQB) throws IctkException {

        boolean toUpdate = false;

        ProgramInfo findProgramInfo = null;

        if (CommonUtils.hasValue(programInfoSaveRQB.getProgId())) {

            toUpdate = true;
            findProgramInfo = programInfoRepository.findById(programInfoSaveRQB.getProgId())
                    .orElseThrow((() -> new IctkException(trId, AppCode.PROGRAM_PROC_ERROR, "프로그램 " + programInfoSaveRQB.getProgId() + " 없음")));

            findProgramInfo.setProgName(programInfoSaveRQB.getProgName());
            findProgramInfo.setDescription(programInfoSaveRQB.getDescription());
            findProgramInfo.setProduct(programInfoSaveRQB.getProduct());
            findProgramInfo.setSessionHandler(programInfoSaveRQB.getSessionHandler());
            findProgramInfo.setTestCode(programInfoSaveRQB.getTestCode());
            findProgramInfo.setEtcOption(String.join("|", programInfoSaveRQB.getEtcOption()));
            findProgramInfo.setProfId(programInfoSaveRQB.getProfId());
            findProgramInfo.setKeyisId(programInfoSaveRQB.getKeyisId());
            findProgramInfo.setScrtId(programInfoSaveRQB.getScrtId());
            findProgramInfo.setEncryptedSn(programInfoSaveRQB.isEncryptedSn());
            findProgramInfo.setCompanyCode(programInfoSaveRQB.getCompanyCode());
            findProgramInfo.setCountryCode(programInfoSaveRQB.getCountryCode());
            findProgramInfo.setInterfaceType(programInfoSaveRQB.getInterfaceType());
            findProgramInfo.setPackageType(programInfoSaveRQB.getPackageType());
            findProgramInfo.setStatus(programInfoSaveRQB.getStatus());
            findProgramInfo.setUpdatedAt(LocalDateTime.now());

        }

        ProgramInfo saveProgramInfo = programInfoRepository.save(
                toUpdate ? findProgramInfo : ProgramInfo.builder()
                        .progName(programInfoSaveRQB.getProgName())
                        .description(programInfoSaveRQB.getDescription())
                        .product(programInfoSaveRQB.getProduct())
                        .sessionHandler(programInfoSaveRQB.getSessionHandler())
                        .testCode(programInfoSaveRQB.getTestCode())
                        .etcOption(String.join("|", programInfoSaveRQB.getEtcOption()))
                        .profId(programInfoSaveRQB.getProfId())
                        .keyisId(programInfoSaveRQB.getKeyisId())
                        .scrtId(programInfoSaveRQB.getScrtId())
                        .isEncryptedSn(programInfoSaveRQB.isEncryptedSn())
                        .companyCode(programInfoSaveRQB.getCompanyCode())
                        .countryCode(programInfoSaveRQB.getCountryCode())
                        .interfaceType(programInfoSaveRQB.getInterfaceType())
                        .packageType(programInfoSaveRQB.getPackageType())
                        .status(programInfoSaveRQB.getStatus())
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return ProgramInfoSaveDTO.ProgramInfoSaveRSB.builder()
                .progId(saveProgramInfo.getProgId())
                .progName(saveProgramInfo.getProgName())
                .build();

    }

    @Override
    @Transactional
    // 프로그램 삭제 서비스
    public ProgramInfoDeleteDTO.ProgramInfoDeleteRSB deleteProgram(String trId, ProgramInfoDeleteDTO.ProgramInfoDeleteRQB programInfoDeleteRQB) throws IctkException {
        programInfoRepository.findById(programInfoDeleteRQB.getProgId())
                .orElseThrow(() -> new IctkException(trId, AppCode.PROGRAM_PROC_ERROR, "프로그램 " + programInfoDeleteRQB.getProgId() + " 없음."));

        long dcnt = programInfoRepository.deleteProgramProgId(programInfoDeleteRQB.getProgId());

        return ProgramInfoDeleteDTO.ProgramInfoDeleteRSB
                .builder()
                .result((dcnt > 0) ? AppConstants.SUCC : AppConstants.FAIL)
                .deleteCnt((int) dcnt)
                .build();
    }

    // 프로그램 Id 목록
    @Override
    public ProgramInfoIdListDTO.ProgramInfoIdListRSB programInfoIdsList(String trId) throws IctkException {

        List<String> progIds = programInfoRepository.findAllProgIds();

        return ProgramInfoIdListDTO.ProgramInfoIdListRSB.builder()
                .programInfoIdList(progIds)
                .build();
    }
}
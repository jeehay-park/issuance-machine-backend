package com.ictk.issuance.service.programinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.config.ConfigDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoDeleteDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoListDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoSaveDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoSearchDTO;
import com.ictk.issuance.data.model.KeyissueConfig;
import com.ictk.issuance.data.model.ProfileConfig;
import com.ictk.issuance.data.model.ScriptConfig;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.properties.ProgramInfoProperties;
import com.ictk.issuance.repository.ProgramInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
                    if(!programInfoRepository.isTableExist(database, table) ) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if(!programInfoRepository.makeTable(database, table)) {
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
                        .etcOption(programInfo.getEtcOption())
                        .profileInfo(getProfileConfigObjList(programInfo.getProfileConfig()))
                        .keyIssueInfo(getKeyissueConfigObjList(programInfo.getKeyIssueInfo()))
                        .scriptInfo(getScriptConfigObjList(programInfo.getScriptInfo()))
                        .isEncryptSn(programInfo.isEncryptedSn())
                        .companyCode(programInfo.getCompanyCode())
                        .countryCode(programInfo.getCountryCode())
                        .interfaceType(programInfo.getInterfaceType())
                        .packageType(programInfo.getPackageType())
                        .createdAt(programInfo.getCreatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                        .build()
                )
                .orElseThrow(()
                        -> new IctkException(trId, AppCode.PROGRAM_PROC_ERROR, "발급기계 "+programInfoSearchRQB.getProgId()+ " 없음."));

        return programInfoSearchRSB;
    }

    // ProfileConfig -> ProfileConfigObj
    private List<ConfigDTO.ProfileConfigObj> getProfileConfigObjList(List<ProfileConfig> profileConfig ) {
        List<ConfigDTO.ProfileConfigObj> profileConfigObjList = new ArrayList<>();
        if(CommonUtils.hasElements(profileConfig)) {
            profileConfig.forEach(item -> {
                profileConfigObjList.add( ConfigDTO.ProfileConfigObj.builder()
                        .profId(item.getProfId())
                        .profName(item.getProfName())
                        .description(item.getDescription())
                        .build() );
            });
        }
        return profileConfigObjList;
    }


    // KeyissueConfig -> KeyissueConfigObj
    private List<ConfigDTO.KeyissueConfigObj> getKeyissueConfigObjList(List<KeyissueConfig> keyissueConfig ) {
        List<ConfigDTO.KeyissueConfigObj> keyissueConfigObjList = new ArrayList<>();
        if(CommonUtils.hasElements(keyissueConfig)) {
            keyissueConfig.forEach(item -> {
                keyissueConfigObjList.add( ConfigDTO.KeyissueConfigObj.builder()
                        .keyisId(item.getKeyisId())
                        .keyisName(item.getKeyisName())
                        .description(item.getDescription())
                        .build() );
            });
        }
        return keyissueConfigObjList;
    }

    // ScriptConfig -> ScriptConfigObj
    private List<ConfigDTO.ScriptConfigObj> getScriptConfigObjList(List<ScriptConfig> scriptConfig ) {
        List<ConfigDTO.ScriptConfigObj> scriptConfigObjList = new ArrayList<>();
        if(CommonUtils.hasElements(scriptConfig)) {
            scriptConfig.forEach(item -> {
                scriptConfigObjList.add( ConfigDTO.ScriptConfigObj.builder()
                        .scrtId(item.getScrtId())
                        .scrtName(item.getScrtName())
                        .description(item.getDescription())
                        .build() );
            });
        }
        return scriptConfigObjList;
    }

    @Override
    // 프로그램 목록 조회 서비스
    public ProgramInfoListDTO.ProgramInfoListRSB fetchProgramList(String trId, ProgramInfoListDTO.ProgramInfoListRQB programInfoListRQB) throws IctkException {
        return null;
    }

    @Override
    // 프로그램 생성 서비스
    public ProgramInfoSaveDTO.ProgramInfoSaveRQB saveProgram(String trId, ProgramInfoSaveDTO.ProgramInfoSaveRQB programInfoSaveRQB) throws IctkException {
        return null;
    }

    @Override
    // 프로그램 삭제 서비스
    public ProgramInfoDeleteDTO.ProgramInfoDeleteRSB deleteProgram(String trId, ProgramInfoDeleteDTO.ProgramInfoDeleteRQB programInfoDeleteRQB) throws IctkException {
        programInfoRepository.findById(programInfoDeleteRQB.getWorkId())
                .orElseThrow(() -> new IctkException(trId, AppCode.PROGRAM_PROC_ERROR, "프로그램 "+programInfoDeleteRQB.getWorkId()+ " 없음."));

        long dcnt = programInfoRepository.deleteProgramProgId(programInfoDeleteRQB.getWorkId());

        return ProgramInfoDeleteDTO.ProgramInfoDeleteRSB
                .builder()
                .result( (dcnt>0) ? AppConstants.SUCC : AppConstants.FAIL )
                .deleteCnt( (int)dcnt)
                .build();
    }



}

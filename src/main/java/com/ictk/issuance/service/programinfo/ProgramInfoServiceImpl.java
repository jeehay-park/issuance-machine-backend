package com.ictk.issuance.service.programinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoDeleteDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoListDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoSaveDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoSearchDTO;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.properties.ProgramInfoProperties;
import com.ictk.issuance.repository.ProgramInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    // 프로그램 정보 조회(단일)
    public ProgramInfoSearchDTO.ProgramInfoSearchRSB searchProgramInfo(String trId, ProgramInfoSearchDTO.ProgramInfoSearchRQB programInfoSearchRQB) throws IctkException {
        return null;
    }

    @Override
    // 프로그램 정보 리스트 가져오기
    public ProgramInfoListDTO.ProgramInfoListRSB fetchProgramInfoList(String trId, ProgramInfoListDTO.ProgramInfoListRQB programInfoListRQB) throws IctkException {
        return null;
    }

    @Override
    // 프로그램 정보 생성/변경
    public ProgramInfoSaveDTO.ProgramInfoSaveRQB saveProgramInfo(String trId, ProgramInfoSaveDTO.ProgramInfoSaveRQB programInfoSaveRQB) throws IctkException {
        return null;
    }

    @Override
    public ProgramInfoDeleteDTO.ProgramInfoDeleteRQB deleteProgramInfo(String trId, ProgramInfoDeleteDTO.ProgramInfoDeleteRQB programInfoDeleteRQB) throws IctkException {
        return null;
    }
}

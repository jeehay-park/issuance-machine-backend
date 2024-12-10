package com.ictk.issuance.service.programinfo;

import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoDeleteDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoDeleteDTO.ProgramInfoDeleteRQB;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoIdListDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoListDTO.ProgramInfoListRQB;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoListDTO.ProgramInfoListRSB;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoSaveDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoSaveDTO.ProgramInfoSaveRQB;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoSearchDTO.ProgramInfoSearchRQB;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoSearchDTO.ProgramInfoSearchRSB;

public interface ProgramInfoService {

    // Program Info 테이블 생성하기
    String createProgramInfoTable();

    // Program Info 단일 정보 조회 서비스
    ProgramInfoSearchRSB searchProgram(String trId, ProgramInfoSearchRQB programInfoSearchRQB) throws IctkException;

    // Program Info 목록 조회 서비스
    ProgramInfoListRSB fetchProgramList(String trId, ProgramInfoListRQB programInfoListRQB) throws IctkException;

    // Program Info 추가/변경 서비스
    ProgramInfoSaveDTO.ProgramInfoSaveRSB saveProgram(String trId, ProgramInfoSaveRQB programInfoSaveRQB) throws IctkException;

    // Program Info 삭제 서비스
    ProgramInfoDeleteDTO.ProgramInfoDeleteRSB deleteProgram(String trId, ProgramInfoDeleteRQB programInfoDeleteRQB) throws IctkException;

    // Program Id 목록 조회
    ProgramInfoIdListDTO.ProgramInfoIdListRSB programInfoIdsList(String trId) throws IctkException;

}

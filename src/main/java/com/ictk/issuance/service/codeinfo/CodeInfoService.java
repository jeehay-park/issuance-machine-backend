package com.ictk.issuance.service.codeinfo;

import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoDeleteDTO;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoDeleteDTO.CodeInfoDeleteRSB;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoListDTO.CodeInfoListRQB;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoListDTO.CodeInfoListRSB;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoSaveDTO.CodeInfoSaveRQB;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoSaveDTO.CodeInfoSaveRSB;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoSearchDTO.CodeInfoSearchRQB;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoSearchDTO.CodeInfoSearchRSB;


public interface CodeInfoService {

    // CODE_INFO 테이블 생성하기
    String createCodeInfoTable();

    // CODE_INFO 리스트 조회 서비스
    CodeInfoListRSB queryCodeInfo(String trId, CodeInfoListRQB listRQB) throws IctkException;

    // CODE_INFO 정보 조회 서비스
   CodeInfoSearchRSB infoCodeInfo(String trId, CodeInfoSearchRQB searchRQB) throws IctkException;

    // CODE_INFO 추가(생성)/변경(편집) 서비스
    CodeInfoSaveRSB saveCodeInfo(String trId, CodeInfoSaveRQB SaveRQB) throws IctkException;

    // CODE_INFO 삭제 서비스
    CodeInfoDeleteRSB deleteCodeInfo(String trId, CodeInfoDeleteDTO.CodeInfoDeleteRQB deleteRQB) throws IctkException;

}

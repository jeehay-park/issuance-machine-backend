package com.ictk.issuance.service.codeenum;

import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.codeenum.CodeEnumDeleteDTO;
import com.ictk.issuance.data.dto.codeenum.CodeEnumSaveDTO;

public interface CodeEnumService {

    // CODE_ENUM 테이블 생성하기
    String createCodeEnumTable();

    // CODE_ENUM 생성/변경 서비스
    CodeEnumSaveDTO.CodeEnumSaveRSB saveCodeEnum(String trId, CodeEnumSaveDTO.CodeEnumSaveRQB codeEnumRQB) throws IctkException;

    // CODE_ENUM 삭제 서비스
    CodeEnumDeleteDTO.CodeEnumDeleteRSB deleteCodeEnum(String trId, CodeEnumDeleteDTO.CodeEnumDeleteRQB codeEnumDeleteRQB) throws IctkException;
}

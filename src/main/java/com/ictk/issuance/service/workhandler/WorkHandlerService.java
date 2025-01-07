package com.ictk.issuance.service.workhandler;

import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.workhandler.WorkHandlerDeleteDTO;
import com.ictk.issuance.data.dto.workhandler.WorkHandlerListDTO;
import com.ictk.issuance.data.dto.workhandler.WorkHandlerSaveDTO;

public interface WorkHandlerService {

    // service 생성 전 dto 필요
    // Work Handler 테이블 생성하기
    String createWorkHandlerTable();

    // Work Handler 목록 조회 서비스
    WorkHandlerListDTO.WorkHandlerListRSB fetchWorkHandlerList(String trId, WorkHandlerListDTO.WorkHandlerListRQB workHandlerListRQB) throws IctkException;

    // Work Handler 생성 서비스
    WorkHandlerSaveDTO.WorkHandlerSaveRSB saveWorkHandler(String trId, WorkHandlerSaveDTO.WorkHandlerSaveRQB workHandlerSaveRQB) throws IctkException;

    // Work Handler 삭제 서비스
    WorkHandlerDeleteDTO.WorkHandlerDeleteRSB deleteWorkHandler(String trId, WorkHandlerDeleteDTO.WorkHandlerDeleteRQB workHandlerDeleteRQB) throws IctkException;

}

package com.ictk.issuance.service.machine;

import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.machine.MachineIdListDTO;
import com.ictk.issuance.data.dto.machine.MachineListDTO.MachineListRQB;
import com.ictk.issuance.data.dto.machine.MachineListDTO.MachineListRSB;
import com.ictk.issuance.data.dto.machine.MachineSaveDTO.MachineSaveRQB;
import com.ictk.issuance.data.dto.machine.MachineSaveDTO.MachineSaveRSB;
import com.ictk.issuance.data.dto.machine.MachineSearchDTO.MachineSearchRQB;
import com.ictk.issuance.data.dto.machine.MachineSearchDTO.MachineSearchRSB;
import com.ictk.issuance.data.dto.machine.MachineDeleteDTO.MachineDeleteRQB;
import com.ictk.issuance.data.dto.machine.MachineDeleteDTO.MachineDeleteRSB;

public interface MachineService {

    // MACHINE 테이블 생성하기
    String createMachineTable();

    // MACHINE 리스트 조회 서비스
    MachineListRSB queryMachines(String trId, MachineListRQB mcnListRQB) throws IctkException;

    // MACHINE 단일 정보 조회 서비스
    MachineSearchRSB infoMachine(String trId, MachineSearchRQB mcnRQB) throws IctkException;

    // MACHINE 추가(생성)/변경(편집) 서비스
    MachineSaveRSB saveMachine(String trId, MachineSaveRQB mcnSaveRQB) throws IctkException;

    // MACHINE 삭제 서비스
    MachineDeleteRSB deleteMachine(String trId, MachineDeleteRQB mcnDelRQB) throws IctkException;

    // MACHINE Id 목록 조회
    MachineIdListDTO.MachineIdListRSB machineIdsList(String trId) throws IctkException;
}

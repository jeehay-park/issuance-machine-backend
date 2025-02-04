package com.ictk.issuance.service.device;

import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.device.DeviceIdListDTO;
import com.ictk.issuance.data.dto.device.DeviceSaveDTO.DeviceSaveRQB;
import com.ictk.issuance.data.dto.device.DeviceSaveDTO.DeviceSaveRSB;
import com.ictk.issuance.data.dto.device.DeviceDeleteDTO.DeviceDeleteRQB;
import com.ictk.issuance.data.dto.device.DeviceDeleteDTO.DeviceDeleteRSB;
import com.ictk.issuance.data.dto.machine.MachineIdListDTO;

public interface DeviceService {

    // 디바이스 테이블 생성하기
    String createDeviceTable();

    // 디바이스 추가(생성)/변경(편집) 서비스
    DeviceSaveRSB saveDevices(String trId, DeviceSaveRQB dvcsSaveRQB) throws IctkException;

    // 디바이스 삭제 서비스
    DeviceDeleteRSB deleteDevices(String trId, DeviceDeleteRQB dvcsDelRQB) throws IctkException;

    // 디바이스 Id 목록 조회
    DeviceIdListDTO.DeviceIdListRSB deviceIdsList(String trId) throws IctkException;
}

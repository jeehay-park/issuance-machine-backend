package com.ictk.issuance.service.device;

import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.device.DeviceSaveDTO.DeviceSaveRQB;
import com.ictk.issuance.data.dto.device.DeviceSaveDTO.DeviceSaveRSB;
import com.ictk.issuance.data.dto.device.DeviceDeleteDTO.DeviceDeleteRQB;
import com.ictk.issuance.data.dto.device.DeviceDeleteDTO.DeviceDeleteRSB;


public interface DeviceService {

    // MACHINE_DEVICE 테이블 생성하기
    String createDeviceTable();


    // MACHINE_DEVICE 추가(생성)/변경(편집) 서비스
    // @Transactional
    DeviceSaveRSB saveDevices(String trId, DeviceSaveRQB dvcsSaveRQB) throws IctkException;

    // MACHINE 삭제 서비스
    DeviceDeleteRSB deleteDevices(String trId, DeviceDeleteRQB dvcsDelRQB) throws IctkException;

}

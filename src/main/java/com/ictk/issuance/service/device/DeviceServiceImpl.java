package com.ictk.issuance.service.device;

import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.device.DeviceDeleteDTO.DeviceDeleteRQB;
import com.ictk.issuance.data.dto.device.DeviceDeleteDTO.DeviceDeleteRSB;
import com.ictk.issuance.data.dto.device.DeviceSaveDTO.DeviceSaveRQB;
import com.ictk.issuance.data.dto.device.DeviceSaveDTO.DeviceSaveRSB;
import com.ictk.issuance.data.model.Device;
import com.ictk.issuance.data.model.Machine;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.properties.DeviceProperties;
import com.ictk.issuance.repository.DeviceRepository;
import com.ictk.issuance.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceServiceImpl implements DeviceService {

    private final IssuanceManager issuanceManager;

    private final DBProperties dbProperties;

    private final DeviceProperties deviceProperties;

    private final MachineRepository machineRepository;
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    // MACHINE_DEVICE 테이블 생성하기
    public String createDeviceTable() {
        return issuanceManager.createTable( dbProperties.database(), deviceProperties.tableName(),
                (database, table) -> {
                    if ( !deviceRepository.isTableExist(database, table) ) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if ( !deviceRepository.makeTable(database, table)) {
                            log.error("***** "+table+" 테이블 생성에 실패했습니다.");
                            return "FAIL";
                        } else
                            return "SUCC";
                    } else {
                        log.info("----- " + table + " 테이블이 존재합니다. ");
                        return "EXIST";
                    }
                });
    }


    @Override
    // @Modifying(flushAutomatically = false)
    @Transactional(rollbackFor = { IctkException.class, Exception.class })
    // MACHINE_DEVICE 추가(생성)/변경(편집) 서비스
    public DeviceSaveRSB saveDevices(String trId, DeviceSaveRQB dvcsSaveRQB) throws IctkException {
        Machine findMachine = machineRepository.findMachineByMcnId( dvcsSaveRQB.getMcnId() )
                .orElseThrow( () -> new IctkException(trId, AppCode.DEVICE_PROC_ERROR, "발급기계 "+dvcsSaveRQB.getMcnId()+ " 없음.") );

        if (!CommonUtils.hasElements(dvcsSaveRQB.getDeviceList()))
            throw new IctkException(trId, AppCode.DEVICE_PROC_ERROR, "디바이스 정보가 없음.");


        // deviceList 검증
        // dvcNum값 중복이 없고, dvcNum의 최대값이 deviceList 사이즈와 동일한지 확인
        boolean listVadid = true;
        Set<Integer> numbers = new TreeSet<>();

        dvcsSaveRQB.getDeviceList().forEach( procDvc -> numbers.add(procDvc.getDvcNum()));
        // TreeSet이 정렬된 상태로 저장하기 때문에 첫 번째 값이 최소, 마지막 값이 최대입니다.
        Integer min = ((TreeSet<Integer>) numbers).first();
        Integer max = ((TreeSet<Integer>) numbers).last();
        if(min != 1 || max != dvcsSaveRQB.getDeviceList().size())
            throw new IctkException(trId, AppCode.DEVICE_PROC_ERROR, "디바이스 번호 오류.");

        // Assuming dvcsSaveRQB has a method to get mcnId
        String mcnId = dvcsSaveRQB.getMcnId();

        // Fetch the highest dvcNum for the given mcnId
        Integer maxDvcNum = deviceRepository.findMaxDvcNumByMcnId(mcnId).orElse(0);

        // Start assigning new dvcNum values from maxDvcNum + 1
        AtomicInteger currentDvcNum = new AtomicInteger(maxDvcNum + 1);

        dvcsSaveRQB.getDeviceList().forEach(procDvc -> {
            // Assign a new dvcNum to each device starting from maxDvcNum + 1
            procDvc.setDvcNum(currentDvcNum.getAndIncrement());
        });

        AtomicInteger updateCnt = new AtomicInteger(0);
        List<Device> toDevices = new ArrayList<>();
        dvcsSaveRQB.getDeviceList().forEach( procDvc -> {
            try {
                // 데이터가 있으면 갱신
                Device findDevice = null;
                if(CommonUtils.hasValue(procDvc.getDvcId())) {
                    findDevice = deviceRepository.findDeviceByDvcId( procDvc.getDvcId() )
                            .orElseThrow( () -> new IctkException(trId, AppCode.DEVICE_PROC_ERROR, "디바이스 "+procDvc.getDvcId()+ " 없음.") );
                    findDevice.setDvcName( procDvc.getDvcName() );
                    findDevice.setDvcNum( procDvc.getDvcNum() );
                    findDevice.setIp( procDvc.getIp() );
                    findDevice.setRomVer( procDvc.getRomVer() );
                    findDevice.setUpdatedAt(LocalDateTime.now());

                } else {
                    if (deviceRepository.existsByMcnIdAndDvcNum( dvcsSaveRQB.getMcnId(), procDvc.getDvcNum() ))
                        throw new IctkException(trId, AppCode.DEVICE_PROC_ERROR, "중복되는 디바이스 번호("+procDvc.getDvcNum()+")가 존재함.");
                }


                Device saveDevice = deviceRepository.save(findDevice != null ? findDevice :
                        Device.builder()
                                .machine(findMachine)
                                .dvcName(procDvc.getDvcName())
                                .dvcNum(procDvc.getDvcNum())
                                .ip(procDvc.getIp())
                                .romVer(procDvc.getRomVer())
                                .createdAt(LocalDateTime.now())
                                .build());

                log.info("Saved device with ID: {}", saveDevice.getDvcId());

                if (saveDevice.getDvcId() != null)
                    updateCnt.getAndIncrement();

            } catch (Exception e ) {
                log.error("error ***** {}", e.getMessage());
                throw new IctkException(trId, AppCode.DEVICE_PROC_ERROR, "디바이스 DB SAVE 오류.");
            }

        } );

        // deviceRepository.flush();

        // 추가작업
        // 해당 발급기계의 max값 보다 큰 디바이스를 삭제??

        return DeviceSaveRSB.builder()
                .result( updateCnt.get() == max ? AppConstants.SUCC : AppConstants.FAIL )
                .updateCnt( updateCnt.get() )
                .build();
    }

    // MACHINE 삭제 서비스
    @Override
    @Transactional
    public DeviceDeleteRSB deleteDevices(String trId, DeviceDeleteRQB dvcsDelRQB) throws IctkException {
        if( !CommonUtils.hasElements(dvcsDelRQB.getDvcIds()))
            throw new IctkException(trId, AppCode.REQ_BODY_ERROR, "dvcIds is empty.");

        var delDvcs = deviceRepository.findAllById( dvcsDelRQB.getDvcIds() );
        if( !CommonUtils.hasElements(delDvcs) || delDvcs.size()!= dvcsDelRQB.getDvcIds().size())
            throw new IctkException(trId, AppCode.DEVICE_PROC_ERROR, "디바이스 ID 확인필요");

        try {
            deviceRepository.deleteAllInBatch( delDvcs );
        } catch (Exception e ) {
            log.error("error ***** {}", e.getMessage());
            throw new IctkException(trId, AppCode.DEVICE_PROC_ERROR, "디바이스 DB 삭제 오류.");
        }
        return DeviceDeleteRSB.builder()
                .result( AppConstants.SUCC )
                .deleteCnt( delDvcs.size() )
                .build();
    }

}

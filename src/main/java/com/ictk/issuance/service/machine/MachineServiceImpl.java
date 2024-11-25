package com.ictk.issuance.service.machine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.device.DeviceDTO.DeviceObj;
import com.ictk.issuance.data.dto.machine.MachineListDTO.MachineListRQB;
import com.ictk.issuance.data.dto.machine.MachineListDTO.MachineListRSB;
import com.ictk.issuance.data.dto.machine.MachineSaveDTO.MachineSaveRQB;
import com.ictk.issuance.data.dto.machine.MachineSaveDTO.MachineSaveRSB;
import com.ictk.issuance.data.dto.machine.MachineSearchDTO.MachineSearchRQB;
import com.ictk.issuance.data.dto.machine.MachineSearchDTO.MachineSearchRSB;
import com.ictk.issuance.data.dto.machine.MachineDeleteDTO.MachineDeleteRQB;
import com.ictk.issuance.data.dto.machine.MachineDeleteDTO.MachineDeleteRSB;
import com.ictk.issuance.data.dto.shared.AppDTO.HeaderInfoObj;
import com.ictk.issuance.data.model.Device;
import com.ictk.issuance.data.model.Machine;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.properties.MachineProperties;
import com.ictk.issuance.repository.MachineRepository;
import com.ictk.issuance.utils.AppHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import io.vavr.Tuple2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ictk.issuance.data.model.QMachine.machine;

@Slf4j
@RequiredArgsConstructor
@Service
public class MachineServiceImpl implements MachineService {

    private final IssuanceManager issuanceManager;

    private final DBProperties dbProperties;

    private final MachineProperties machineProperties;

    private final MachineRepository machineRepository;

    private final ObjectMapper objectMapper;


    @Override
    @Transactional
    // MACHINE 테이블 생성하기
    public String createMachineTable() {

        return issuanceManager.createTable( dbProperties.database(), machineProperties.tableName(),
                (database, table) -> {
                    if ( !machineRepository.isTableExist(database, table) ) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if ( !machineRepository.makeTable(database, table)) {
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


    // MACHINE 리스트 조회 서비스
    @Override
    public MachineListRSB queryMachines(String trId, MachineListRQB mcnListRQB) throws IctkException {

        // 데이터 헤더 정보 구성
        Map<String, HeaderInfoObj> hdrInfoMap = AppHelper.machineHeaderInfoMap();
        final List<HeaderInfoObj> headerInfos = new ArrayList<HeaderInfoObj>();
        hdrInfoMap.forEach( (k,v) -> { headerInfos.add(v); } );

        // filter가 있는 경우에 기존 filterArrAndOr, filterArr는 무시되고 filter값이 우선시 된다.
        if(CommonUtils.hasValue(mcnListRQB.getFilter())) {
            var applied = AppHelper.applyFilterToRequestRQB( hdrInfoMap, mcnListRQB.getFilter() );
            mcnListRQB.setFilterArrAndOr(applied._1());
            mcnListRQB.setFilterArr(applied._2());
            log.debug("Filter checked! {} {}", mcnListRQB.getFilterArrAndOr(), CommonUtils.toJson(objectMapper, mcnListRQB.getFilterArr()));
        }

        // 동적 소팅 생성
        List<OrderSpecifier> orderSpecifiers = AppHelper.getRequestRQBOrderSpecifiers(
                Machine.class, "machine", hdrInfoMap, mcnListRQB.getSortKeyName(), mcnListRQB.getOrder() );

        // 동적 쿼리 생성
        BooleanBuilder queryConds = new BooleanBuilder();
        if(CommonUtils.hasValue(mcnListRQB.getMcnName()))
            queryConds.and(machine.mcnName.contains( mcnListRQB.getMcnName() ));

        BooleanBuilder filterConds = AppHelper.composeFilterDynamicConditions(
                mcnListRQB.getFilterArrAndOr(),
                mcnListRQB.getFilterArr(),
                (kname) -> {
                    switch(kname) {
                        case "mcn_name" -> { return machine.mcnName; }
                        case "etc" -> { return machine.etc; }
                    }
                    return null;
                } );

        if(filterConds!=null)
            queryConds.and(filterConds);

        StopWatch timer = new StopWatch();
        timer.start();

        // 테이블 쿼리
        Tuple2<Long, Page<Machine>> machinesPaged = machineRepository.getMachinePageByCondition(queryConds, mcnListRQB.getPageble(), orderSpecifiers);

        timer.stop();
        log.info("Query took {} nanos ", (int)Math.round(timer.getTotalTimeNanos()) );

        return MachineListRSB.builder()
                .totalCnt( machinesPaged._1() )
                .curPage( mcnListRQB.getPageble().getPageNumber() )
                .headerInfos( mcnListRQB.isHeaderInfo() ? headerInfos : null )
                .machineList( composeMachineList(hdrInfoMap, machinesPaged._2().toList()) )
                .build();

    }

    private List<Map<String,Object>> composeMachineList(Map<String, HeaderInfoObj> hdrInfoMap, List<Machine> machines) {
        final List<Map<String,Object>> bodyList = new ArrayList<>();
        if(hdrInfoMap!=null && machines!=null) {
            AtomicInteger idx = new AtomicInteger(1);
            machines.forEach( machine -> {
                Map<String,Object> dataMap = new LinkedHashMap<>();
                dataMap.put("idx", idx.get() );
                hdrInfoMap.forEach( (k,v) -> {
                    switch(v.getKeyName()) {
                        case "mcn_id" -> dataMap.put(v.getKeyName(), machine.getMcnId());
                        case "mcn_name" -> dataMap.put(v.getKeyName(), machine.getMcnName());
                        case "etc" -> dataMap.put(v.getKeyName(), machine.getEtc());
                        case "updated_at" -> dataMap.put(v.getKeyName(), machine.getUpdatedAt()
                                .format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ));
                        case "created_at" -> dataMap.put(v.getKeyName(), machine.getCreatedAt()
                                .format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ));
                        default -> {}
                    }
                });
                bodyList.add(dataMap);
                idx.getAndIncrement();
            } );
        }
        return bodyList;
    }

    // MACHINE 단일 정보 조회 서비스
    @Override
    @Transactional  // 중간에 machine.getDevices()메소드로 lazy 로딩하는 부분이 있으므로 한 트랜잭션으로 묶어줘야 한다.
                    // 그렇지 않은 경우, LazyInitializationException이 발생할 수 있다.
    public MachineSearchRSB infoMachine(String trId, MachineSearchRQB mcnRQB) throws IctkException {

        MachineSearchRSB mcnRSB = machineRepository.findMachineByMcnId( mcnRQB.getMcnId() )
                .map( machine -> MachineSearchRSB.builder()
                        .mcnId(machine.getMcnId())
                        .mcnName(machine.getMcnName())
                        .etc(machine.getEtc())
                        .deviceList( composeDeviceObjList(machine.getDevices()))
                        .createdAt( machine.getCreatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                        .build() )
                .orElseThrow( () -> new IctkException(trId, AppCode.MACHINE_PROC_ERROR, "발급기계 "+mcnRQB.getMcnId()+ " 없음.") );

        return mcnRSB;
    }

    private List<DeviceObj> composeDeviceObjList( List<Device> devices ) {
        List<DeviceObj> deviceObjList = new ArrayList<>();
        if(CommonUtils.hasElements(devices)) {
            devices.forEach(device -> {
               deviceObjList.add( DeviceObj.builder()
                               .dvcId(device.getDvcId())
                               .dvcName(device.getDvcName())
                               .dvcNum(device.getDvcNum())
                               .ip(device.getIp())
                               .romVer(device.getRomVer())
                               .updatedAt(  device.getUpdatedAt()!=null?
                                       device.getUpdatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ):"")
                               .createdAt(  device.getCreatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ))
                       .build() );
            });
        }
        return deviceObjList;
    }


    // MACHINE 추가(생성)/변경(편집) 서비스
    @Override
    @Transactional
    public MachineSaveRSB saveMachine(String trId, MachineSaveRQB mcnSaveRQB) throws IctkException {
        boolean toUpdate = false;

        Machine findMachine = null;
        if(CommonUtils.hasValue(mcnSaveRQB.getMcnId())) {
            toUpdate = true;
            findMachine = machineRepository.findMachineByMcnId( mcnSaveRQB.getMcnId() )
                    .orElseThrow( () -> new IctkException(trId, AppCode.MACHINE_PROC_ERROR, "발급기계 "+mcnSaveRQB.getMcnId()+ " 없음.") );
            findMachine.setMcnName(mcnSaveRQB.getMcnName());
            findMachine.setEtc(mcnSaveRQB.getEtc());
            findMachine.setUpdatedAt(LocalDateTime.now());
        }
        Machine savedMachine = machineRepository.save(
                toUpdate ? findMachine : Machine.builder()
                        .mcnName(mcnSaveRQB.getMcnName())
                        .etc(mcnSaveRQB.getEtc())
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return MachineSaveRSB.builder().mcnId(savedMachine.getMcnId()).build();

    }


    // MACHINE 삭제 서비스
    @Override
    @Transactional
    public MachineDeleteRSB deleteMachine(String trId, MachineDeleteRQB mcnDelRQB) throws IctkException {

        machineRepository.findMachineByMcnId( mcnDelRQB.getMcnId() )
                .orElseThrow( () -> new IctkException(trId, AppCode.MACHINE_PROC_ERROR, "발급기계 "+mcnDelRQB.getMcnId()+ " 없음.") );

        long dcnt = machineRepository.deleteMachineByMcnId( mcnDelRQB.getMcnId() );

        return MachineDeleteRSB.builder()
                .result( (dcnt>0) ? AppConstants.SUCC : AppConstants.FAIL )
                .deleteCnt( (int)dcnt)
                .build();

    }


}



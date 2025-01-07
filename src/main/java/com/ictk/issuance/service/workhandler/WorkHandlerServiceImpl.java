package com.ictk.issuance.service.workhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.shared.AppDTO;
import com.ictk.issuance.data.dto.workhandler.WorkHandlerDeleteDTO;
import com.ictk.issuance.data.dto.workhandler.WorkHandlerListDTO;
import com.ictk.issuance.data.dto.workhandler.WorkHandlerSaveDTO;
import com.ictk.issuance.data.model.WorkHandler;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.properties.WorkHandlerProperties;
import com.ictk.issuance.repository.WorkHandlerRepository;
import com.ictk.issuance.utils.AppHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkHandlerServiceImpl implements WorkHandlerService {

    private final IssuanceManager issuanceManager;

    private final DBProperties dbProperties;

    private final WorkHandlerProperties workHandlerProperties;

    private final WorkHandlerRepository workHandlerRepository;

    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public String createWorkHandlerTable() {
        return issuanceManager.createTable(
                dbProperties.database(),
                workHandlerProperties.tableName(),
                (database, table) -> {
                    if (workHandlerRepository.isTableExist(database, table)) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if (!workHandlerRepository.makeTable(database, table)) {
                            log.info("----- " + table + " 테이블 생성에 실패했습니다. ");
                            return "FAIL";
                        } else {
                            return "SUCC";
                        }
                    } else {
                        log.info("----- " + table + " 테이블이 존재합니다. ");
                        return "EXIST";
                    }
                }
        );
    }

    @Override
    public WorkHandlerListDTO.WorkHandlerListRSB fetchWorkHandlerList(String trId, WorkHandlerListDTO.WorkHandlerListRQB workHandlerListRQB) throws IctkException {

        // 데이터 헤더 정보 구성 <---- AppHelper 작성 필요
        Map<String, AppDTO.HeaderInfoObj> hdrInfoMap = AppHelper.workHandlerHeaderInfoMap();
        final List<AppDTO.HeaderInfoObj> headerInfos = new ArrayList<AppDTO.HeaderInfoObj>();

        hdrInfoMap.forEach((key, value) -> {
            headerInfos.add(value);
        });

        // 테이블 쿼리
        List<WorkHandler> workHandler = workHandlerRepository.findAll();

        return WorkHandlerListDTO.WorkHandlerListRSB.
                builder()
                .workHandlers(composeWorkHandlers(hdrInfoMap, workHandler))
                .build();
    }

    private List<Map<String, Object>> composeWorkHandlers(Map<String, AppDTO.HeaderInfoObj> hdrInfoMap, List<WorkHandler> workHandlers) {
        final List<Map<String, Object>> bodyList = new ArrayList<>();
        if (hdrInfoMap != null && workHandlers != null) {
            AtomicInteger idx = new AtomicInteger(1);
            workHandlers.forEach(workHandler -> {
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("idx", idx.get());
                hdrInfoMap.forEach((key, value) -> {
                    switch (value.getKeyName()) {
                        case "mcnName" -> dataMap.put(value.getKeyName(), workHandler.getDevice().getMachine().getMcnName());
                        case "dvcId" -> dataMap.put(value.getKeyName(), workHandler.getDvcId());
                        case "dvcName" -> dataMap.put(value.getKeyName(), workHandler.getDevice().getDvcName());
                        case "dvcIp" -> dataMap.put(value.getKeyName(), workHandler.getDevice().getIp());
                        case "hdlId" -> dataMap.put(value.getKeyName(), workHandler.getHdlId());
                        case "hdlName" -> dataMap.put(value.getKeyName(), workHandler.getHdlName());
                        case "detailMsg" -> dataMap.put(value.getKeyName(), workHandler.getDetailMsg());
                    }
                });

                bodyList.add(dataMap);
                idx.getAndIncrement();
            });
        }
        return bodyList;
    }

    @Override
    public WorkHandlerSaveDTO.WorkHandlerSaveRSB saveWorkHandler(String trId, WorkHandlerSaveDTO.WorkHandlerSaveRQB workHandlerSaveRQB) throws IctkException {

        boolean toUpdate = false;

        WorkHandler findWorkHandler = null;

        String mcnId = null;

        // Retrieve or compute mcnId
        String dvcId = workHandlerSaveRQB.getDvcId();
        if (dvcId != null || !dvcId.isEmpty()) {
            // Fetch mcnId using user parameters (workId, dvcId, etc.)
            mcnId = workHandlerRepository.findMcnIdByDvcId(workHandlerSaveRQB.getDvcId())
                    .map(device -> device.getMachine()) // Assuming `getMachine()` retrieves the related Machine entity
                    .map(machine -> machine.getMcnId())
                    .orElseThrow(() -> new IctkException(trId, AppCode.WORK_PROC_ERROR, "MCN ID could not be resolved"));
        } else {
            throw new IctkException(trId, AppCode.WORK_PROC_ERROR, "Device ID (dvcId) cannot be null or empty.");
        }

        // 기존의 WorkHandler 변경
        if (CommonUtils.hasValues(workHandlerSaveRQB.getWorkId(), workHandlerSaveRQB.getDvcId())) {
            toUpdate = true;
            findWorkHandler = workHandlerRepository.findWorkHandlerByWorkId(workHandlerSaveRQB.getWorkId())
                    .orElseThrow(() -> new IctkException(trId, AppCode.WORK_PROC_ERROR, "발급 작업 " + workHandlerSaveRQB.getWorkId() + " 없음"));
            findWorkHandler.setHdlId(workHandlerSaveRQB.getHdlName());
            findWorkHandler.setWorkId(workHandlerSaveRQB.getWorkId());
            findWorkHandler.setDvcId(workHandlerSaveRQB.getDvcId());
            findWorkHandler.setMcnId(mcnId);
            findWorkHandler.setUpdatedAt(LocalDateTime.now());
        }

        WorkHandler saveWorkHandler = workHandlerRepository.save(

                toUpdate ?
                        findWorkHandler :
                        WorkHandler.builder()
                                .workId(workHandlerSaveRQB.getWorkId())
                                .dvcId(workHandlerSaveRQB.getDvcId())
                                .hdlName(workHandlerSaveRQB.getHdlName())
                                .mcnId(mcnId)
                                .createdAt(LocalDateTime.now())
                                .build()
        );

        return WorkHandlerSaveDTO.WorkHandlerSaveRSB.builder()
                .hdlId(saveWorkHandler.getHdlId())
                .hdlName(saveWorkHandler.getHdlName())
                .build();
    }

    @Override
    public WorkHandlerDeleteDTO.WorkHandlerDeleteRSB deleteWorkHandler(String trId, WorkHandlerDeleteDTO.WorkHandlerDeleteRQB workHandlerDeleteRQB) throws IctkException {

        workHandlerRepository.findById(workHandlerDeleteRQB.getHdlId())
                .orElseThrow(() -> new IctkException(trId, AppCode.WORK_PROC_ERROR, " 발급 작업 " + workHandlerDeleteRQB.getHdlId() + " 없음."));

        long dcnt = workHandlerRepository.deleteWorkHandlerId(workHandlerDeleteRQB.getHdlId());

        return WorkHandlerDeleteDTO.WorkHandlerDeleteRSB.builder()
                .result((dcnt > 0) ? AppConstants.SUCC : AppConstants.FAIL)
                .deleteCnt((int) dcnt)
                .build();

    }
}

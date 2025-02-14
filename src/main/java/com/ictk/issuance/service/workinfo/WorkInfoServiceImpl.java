package com.ictk.issuance.service.workinfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.machine.MachineIdListDTO;
import com.ictk.issuance.data.dto.shared.AppDTO;
import com.ictk.issuance.data.dto.workinfo.*;
import com.ictk.issuance.data.model.WorkInfo;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.properties.WorkInfoProperties;
import com.ictk.issuance.repository.WorkInfoRepository;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ictk.issuance.data.model.QWorkInfo.workInfo;

@Slf4j
@Service
@RequiredArgsConstructor // Generates a constructor for final fields and fields marked @NonNull.
public class WorkInfoServiceImpl implements WorkInfoService {

    // Declare a final instance variable for IssuanceManager
    private final IssuanceManager issuanceManager;

    private final DBProperties dbProperties;

    private final WorkInfoProperties workInfoProperties;

    private final WorkInfoRepository workInfoRepository;

    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    // 작업 정보 테이블 생성하기
    // @Transactional wraps the annotated method (or class) in a database transaction.
    // If the method completes successfully, the transaction is committed.
    // If an exception occurs, the transaction is rolled back.
    public String createWorkInfoTable() {
        return issuanceManager.createTable(
                dbProperties.database(),
                workInfoProperties.tableName(),
                (database, table) -> {
                    if (!workInfoRepository.isTableExist(database, table)) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if (!workInfoRepository.makeTable(database, table)) {
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
    @Transactional
    // 작업 정보 조회 서비스
    public WorkInfoSearchDTO.WorkInfoSearchRSB searchWorkInfo(String trId, WorkInfoSearchDTO.WorkInfoSearchRQB workInfoSearchRQB) throws IctkException {
        WorkInfoSearchDTO.WorkInfoSearchRSB workInfoSearchRSB = workInfoRepository
                .findById(workInfoSearchRQB.getWorkId())
                .map(workInfo -> WorkInfoSearchDTO.WorkInfoSearchRSB.builder()
                        .workId(workInfo.getWorkId())
                        .workNo(workInfo.getWorkNo())
                        .tagName(workInfo.getTagName())
                        .customer(workInfo.getCustomer())
                        .orderNo(workInfo.getOrderNo())
                        .deviceName(workInfo.getDeviceName())
                        .progId(workInfo.getProgId())
                        .progName(workInfo.getProgramInfo().getProgName())
                        .mcnId(workInfo.getMcnId())
                        .mcnName(workInfo.getMachineInfo().getMcnName())
                        .snrId(workInfo.getSnrId())
                        .snrName(workInfo.getSnRuleInfo().getSnrName())
                        .isLock(workInfo.getIsLock())
                        .status(workInfo.getStatus())
                        .createdAt(workInfo.getCreatedAt().format(DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT)))
                        .build()
                ).orElseThrow(
                        () -> new IctkException(trId, AppCode.WORK_PROC_ERROR, "작업 " + workInfoSearchRQB.getWorkId() + " 없음.")
                );

        return workInfoSearchRSB;
    }

    private boolean convertStringToBoolean(String str) {
        return str.equals("true");
    }

    @Override
    public WorkInfoListDTO.WorkInfoListRSB fetchWorkInfoList(String trId, WorkInfoListDTO.WorkInfoListRQB workInfoListRQB) throws IctkException {

        // 데이터 헤더 정보 구성 <---- AppHelper 작성 필요
        Map<String, AppDTO.HeaderInfoObj> hdrInfoMap = AppHelper.workInfoHeaderInfoMap();
        final List<AppDTO.HeaderInfoObj> headerInfos = new ArrayList<AppDTO.HeaderInfoObj>();

        hdrInfoMap.forEach((key, value) -> {
            headerInfos.add(value);
        });

        // filter가 있는 경우에 기존 filterArrAndOr, filterArr는 무시되고 filter값이 우선시 된다.
        if (CommonUtils.hasValue(workInfoListRQB.getFilter())) {
            var applied = AppHelper.applyFilterToRequestRQB(hdrInfoMap, workInfoListRQB.getFilter());
            workInfoListRQB.setFilterArrAndOr((applied._1()));
            workInfoListRQB.setFilterArr(applied._2());
            log.debug("Filter checked! {} {}", workInfoListRQB.getFilterArrAndOr(), CommonUtils.toJson(objectMapper, workInfoListRQB.getFilterArr()));
        }

        // 동적 소팅 생성
        List<OrderSpecifier> orderSpecifiers = AppHelper.getRequestRQBOrderSpecifiers(
                WorkInfo.class, "workInfo", hdrInfoMap, workInfoListRQB.getSortKeyName(), workInfoListRQB.getOrder()
        );

        // 동적 쿼리 생성
        BooleanBuilder queryConds = new BooleanBuilder();
        if (CommonUtils.hasValue(workInfoListRQB.getWorkNo()))
            queryConds.and(workInfo.workNo.contains(workInfoListRQB.getProgName()));

        BooleanBuilder filterConds = AppHelper.composeFilterDynamicConditions(
                workInfoListRQB.getFilterArrAndOr(),
                workInfoListRQB.getFilterArr(),
                (keyName) -> {
                    switch (keyName) {
                        case "work_no" -> {
                            return workInfo.workNo;
                        }
                        case "tag_name" -> {
                            return workInfo.tagName;
                        }
                    }
                    return null;
                });

        if (filterConds != null) {
            queryConds.and(filterConds);
        }

        StopWatch timer = new StopWatch();
        timer.start();

        // 테이블 쿼리
        Tuple2<Long, Page<WorkInfo>> workInfoPaged = workInfoRepository.getWorkInfoPageByCondition(
                queryConds,
                workInfoListRQB.getPageable(),
                orderSpecifiers
        );

        timer.stop();
        log.info("Query took {} nanos ", (int) Math.round(timer.getTotalTimeNanos()));

        return WorkInfoListDTO.WorkInfoListRSB.builder()
                .totalCnt(workInfoPaged._1())
                .curPage(workInfoListRQB.getPageable().getPageNumber())
                .headerInfos(workInfoListRQB.isHeaderInfo() ? headerInfos : null)
                .workList(composeWorkInfoList(hdrInfoMap, workInfoPaged._2().toList()))
                .build();
    }

    private List<Map<String, Object>> composeWorkInfoList(Map<String, AppDTO.HeaderInfoObj> hdrInfoMap, List<WorkInfo> workInfos) {
        final List<Map<String, Object>> bodyList = new ArrayList<>();
        if(hdrInfoMap != null && workInfos != null) {
            AtomicInteger idx = new AtomicInteger(1);
            workInfos.forEach(workInfo -> {
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("idx", idx.get());
                hdrInfoMap.forEach((key, value) -> {
                    switch (value.getKeyName()) {
                        case "work_id" -> dataMap.put(value.getKeyName(), workInfo.getWorkId());
                        case "work_no" -> dataMap.put(value.getKeyName(), workInfo.getWorkNo());
                        case "tag_name" -> dataMap.put(value.getKeyName(), workInfo.getTagName());
                        case "customer" -> dataMap.put(value.getKeyName(), workInfo.getCustomer());
                        case "device_name" -> dataMap.put(value.getKeyName(), workInfo.getDeviceName());
                        case "order_no" -> dataMap.put(value.getKeyName(), workInfo.getOrderNo());
                        case "prog_id" -> dataMap.put(value.getKeyName(), workInfo.getProgId());
                        case "mcn_id" -> dataMap.put(value.getKeyName(), workInfo.getMcnId());
                        case "snr_id" -> dataMap.put(value.getKeyName(), workInfo.getSnrId());
                        case "target_size" -> dataMap.put(value.getKeyName(), workInfo.getTargetSize());
                        case "completed_size" -> dataMap.put(value.getKeyName(), workInfo.getCompletedSize());
                        case "failed_size" -> dataMap.put(value.getKeyName(), workInfo.getFailedSize());
                        case "check_size" -> dataMap.put(value.getKeyName(), workInfo.getCheckSize());
                        case "due_date" -> dataMap.put(value.getKeyName(), workInfo.getDueDate());
                        case "description" -> dataMap.put(value.getKeyName(), workInfo.getDescription());
                        case "is_lock" -> dataMap.put(value.getKeyName(), workInfo.getIsLock());
                        case "status" -> dataMap.put(value.getKeyName(), workInfo.getStatus());
                        case "param" -> dataMap.put(value.getKeyName(), workInfo.getParam());
                        case "param_ext" -> dataMap.put(value.getKeyName(), workInfo.getParamExt());
                        case "detail_msg" -> dataMap.put(value.getKeyName(), workInfo.getDetailMsg());
                        case "started_at" -> dataMap.put(value.getKeyName(), workInfo.getStartedAt());
                        case "updated_at" -> dataMap.put(value.getKeyName(), workInfo.getUpdatedAt().format(DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT)));
                        case "created_at" -> dataMap.put(value.getKeyName(), workInfo.getCreatedAt().format(DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT)));
                        case "comment" -> dataMap.put(value.getKeyName(), workInfo.getComment());
                        default -> {

                        }

                    }
                });

                bodyList.add(dataMap);
                idx.getAndIncrement();
            });
        }
        return bodyList;
    }

    @Override
    @Transactional
    // 작업 생성/변경 서비스
    public WorkInfoSaveDTO.WorkInfoSaveRSB saveWorkInfo(String trId, WorkInfoSaveDTO.WorkInfoSaveRQB workInfoSaveRQB) throws IctkException {

        boolean toUpdate = false;

        WorkInfo findWorkInfo = null;

        // 기존의 workInfo 변경
        if(CommonUtils.hasValue(workInfoSaveRQB.getWorkId())) {
            toUpdate = true;
            findWorkInfo = workInfoRepository.findById(workInfoSaveRQB.getWorkId())
                    .orElseThrow(() -> new IctkException(trId, AppCode.WORK_PROC_ERROR, "발급 작업 " + workInfoSaveRQB.getWorkId() + " 없음"));

            findWorkInfo.setWorkNo(workInfoSaveRQB.getWorkNo());
            findWorkInfo.setTagName(workInfoSaveRQB.getTagName());
            findWorkInfo.setCustomer(workInfoSaveRQB.getCustomer());
            findWorkInfo.setOrderNo(workInfoSaveRQB.getOrderNo());
            findWorkInfo.setDeviceName(workInfoSaveRQB.getDeviceName());
            findWorkInfo.setProgId(workInfoSaveRQB.getProgId());
            findWorkInfo.setMcnId(workInfoSaveRQB.getMcnId());
            findWorkInfo.setSnrId(workInfoSaveRQB.getSnrId());
            findWorkInfo.setIsLock(workInfoSaveRQB.getIsLock());
            findWorkInfo.setTargetSize(workInfoSaveRQB.getTargetQnty());
            findWorkInfo.setDueDate(workInfoSaveRQB.getDueDate());
            findWorkInfo.setCreatedAt(LocalDateTime.now());

        }

        WorkInfo saveWorkInfo = workInfoRepository.save(
                toUpdate ? findWorkInfo
                        : WorkInfo.builder()
                        .workNo((workInfoSaveRQB.getWorkNo()))
                        .tagName(workInfoSaveRQB.getTagName())
                        .customer(workInfoSaveRQB.getCustomer())
                        .orderNo(workInfoSaveRQB.getOrderNo())
                        .deviceName(workInfoSaveRQB.getDeviceName())
                        .progId(workInfoSaveRQB.getProgId())
                        .mcnId(workInfoSaveRQB.getMcnId())
                        .snrId(workInfoSaveRQB.getSnrId())
                        .isLock(workInfoSaveRQB.getIsLock())
                        .targetSize(workInfoSaveRQB.getTargetQnty())
                        .dueDate(workInfoSaveRQB.getDueDate())
                        .createdAt(LocalDateTime.now())
                        .build()

        );


        return WorkInfoSaveDTO.WorkInfoSaveRSB.builder()
                .workId(saveWorkInfo.getWorkId())
                .workNo(saveWorkInfo.getWorkNo())
                .build();
    }

    @Override
    // 작업 삭제 서비스
    public WorkInfoDeleteDTO.WorkInfoDeleteRSB deleteWorkInfo(String trId, WorkInfoDeleteDTO.WorkInfoDeleteRQB workInfoDeleteRQB) throws IctkException {

        workInfoRepository.findById(workInfoDeleteRQB.getWorkId())
                .orElseThrow(() -> new IctkException(trId, AppCode.WORK_PROC_ERROR, " 발급 작업 " + workInfoDeleteRQB.getWorkId() + " 없음."));

        long dcnt = workInfoRepository.deleteWorkId(workInfoDeleteRQB.getWorkId());

        return WorkInfoDeleteDTO.WorkInfoDeleteRSB.builder()
                .result((dcnt > 0) ? AppConstants.SUCC : AppConstants.FAIL)
                .deleteCnt((int) dcnt)
                .build();
    }

    @Override
    public WorkIdListDTO.WorkIdListRSB workIdsList(String trId) throws IctkException {
        List<String> workIds = workInfoRepository.findAllWorkIds();

        return WorkIdListDTO.WorkIdListRSB.builder()
                .workIdList(workIds)
                .build();
    }

    @Override
    public WorkControlDTO.WorkControlRSB controlWork(String trId, WorkControlDTO.WorkControlRQB workControlRQB) throws IctkException {

        int updatedRows = workInfoRepository.updateWorkStatusByWorkId(workControlRQB.getWorkId(), workControlRQB.getCommand());

        if(updatedRows > 0 ) {
            return WorkControlDTO.WorkControlRSB.builder()
                    .commandResult("SUCC")
                    .resultMessage("Work status updated successfully")
                    .build();
        } else {
            return WorkControlDTO.WorkControlRSB.builder()
                    .commandResult("FAIL")
                    .resultMessage("WorkId not found or updated failed")
                    .build();
        }
    }
}

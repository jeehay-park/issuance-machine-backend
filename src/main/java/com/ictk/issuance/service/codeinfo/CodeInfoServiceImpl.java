package com.ictk.issuance.service.codeinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoDeleteDTO;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoListDTO;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoListDTO.CodeInfoListRSB;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoSaveDTO;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoSearchDTO;
import com.ictk.issuance.data.dto.shared.AppDTO;
import com.ictk.issuance.data.model.CodeInfo;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.CodeInfoProperties;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.repository.CodeInfoRepository;
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

import static com.ictk.issuance.data.model.QCodeInfo.codeInfo;

@Slf4j
@RequiredArgsConstructor
@Service
public class CodeInfoServiceImpl implements CodeInfoService {

    private final IssuanceManager issuanceManager;

    private final DBProperties dbProperties;

    private final CodeInfoProperties codeInfoProperties;

    private final CodeInfoRepository codeInfoRepository;

    private final ObjectMapper objectMapper;


    @Override
    @Transactional
    // CODE_INFO 테이블 생성하기
    public String createCodeInfoTable() {
        return issuanceManager.createTable( dbProperties.database(), codeInfoProperties.tableName(),
                (database, table) -> {
                    if ( !codeInfoRepository.isTableExist(database, table) ) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if ( !codeInfoRepository.makeTable(database, table)) {
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

    @Transactional
    @Override
    public CodeInfoListDTO.CodeInfoListRSB queryCodeInfo(String trId, CodeInfoListDTO.CodeInfoListRQB listRQB) throws IctkException {
        // 데이터 헤더 정보 구성
        Map<String, AppDTO.HeaderInfoObj> hdrInfoMap = AppHelper.codeInfoHeaderInfoMap();
        final List<AppDTO.HeaderInfoObj> headerInfos = new ArrayList<AppDTO.HeaderInfoObj>();
        hdrInfoMap.forEach( (k,v) -> { headerInfos.add(v); } );

        // filter가 있는 경우에 기존 filterArrAndOr, filterArr는 무시되고 filter값이 우선시 된다.
        if(CommonUtils.hasValue(listRQB.getFilter())) {
            var applied = AppHelper.applyFilterToRequestRQB( hdrInfoMap, listRQB.getFilter() );
            listRQB.setFilterArrAndOr(applied._1());
            listRQB.setFilterArr(applied._2());
            log.debug("Filter checked! {} {}", listRQB.getFilterArrAndOr(), CommonUtils.toJson(objectMapper, listRQB.getFilterArr()));
        }

        // 동적 소팅 생성
        List<OrderSpecifier> orderSpecifiers = AppHelper.getRequestRQBOrderSpecifiers(
                CodeInfo.class, "codeInfo", hdrInfoMap, listRQB.getSortKeyName(), listRQB.getOrder() );

        // 동적 쿼리 생성
        BooleanBuilder queryConds = new BooleanBuilder();
        if(CommonUtils.hasValue(listRQB.getCodeName()))
            queryConds.and(codeInfo.codeName.contains( listRQB.getCodeName() ));
        if(CommonUtils.hasValue(listRQB.getCodeGroup()))
            queryConds.and(codeInfo.codeGroup.contains( listRQB.getCodeGroup() ));

        BooleanBuilder filterConds = AppHelper.composeFilterDynamicConditions(
                listRQB.getFilterArrAndOr(),
                listRQB.getFilterArr(),
                (kname) -> {
                    switch(kname) {
                        case "code_name" -> { return codeInfo.codeName; }
                        case "code_group" -> { return codeInfo.codeGroup; }
                    }
                    return null;
                } );
        if(filterConds!=null)
            queryConds.and(filterConds);

        StopWatch timer = new StopWatch();
        timer.start();
        // DB code_info 테이블 쿼리
        Tuple2<Long, Page<CodeInfo>> codeInfoPaged = codeInfoRepository.getCodeInfoPageByCondition(queryConds, listRQB.getPageble(), orderSpecifiers);
        timer.stop();
        log.info("Query tooks {} nanos ", (int)Math.round(timer.getTotalTimeNanos()) );

        return CodeInfoListRSB.builder()
                .totalCnt( codeInfoPaged._1() )
                .curPage( listRQB.getPageble().getPageNumber() )
                .headerInfos( listRQB.isHeaderInfo() ? headerInfos : null )
                .codeInfoList( composeCodeInfoList(hdrInfoMap, codeInfoPaged._2().toList()) )
                .build();
    }

    // 코드 정보 리스트 res body
    private List<Map<String,Object>> composeCodeInfoList(Map<String, AppDTO.HeaderInfoObj> hdrInfoMap, List<CodeInfo> codeInfos) {

        final List<Map<String,Object>> bodyList = new ArrayList<>();
        if(hdrInfoMap!=null && codeInfos!=null) {
            AtomicInteger idx = new AtomicInteger(1);
            codeInfos.forEach( codeInfo -> {
                Map<String,Object> dataMap = new LinkedHashMap<>();
                dataMap.put("idx", idx.get() );
                hdrInfoMap.forEach( (k,v) -> {
                    switch(v.getKeyName()) {
                        case "code_id" -> dataMap.put(v.getKeyName(), codeInfo.getCodeId());
                        case "code_name" -> dataMap.put(v.getKeyName(), codeInfo.getCodeName());
                        case "code_group" -> dataMap.put(v.getKeyName(), codeInfo.getCodeGroup());
                        case "description" -> dataMap.put(v.getKeyName(), codeInfo.getDescription());
                        case "status" -> dataMap.put(v.getKeyName(), codeInfo.getStatus());
                        case "created_at" -> dataMap.put(v.getKeyName(), codeInfo.getCreatedAt()
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

    @Override
    public CodeInfoSearchDTO.CodeInfoSearchRSB infoCodeInfo(String trId, CodeInfoSearchDTO.CodeInfoSearchRQB searchRQB) throws IctkException {
        CodeInfoSearchDTO.CodeInfoSearchRSB searchRSB = codeInfoRepository.findCodeInfoByCodeId( searchRQB.getCodeId() )
                .map( codeInfo -> CodeInfoSearchDTO.CodeInfoSearchRSB.builder()
                        .codeId(codeInfo.getCodeId())
                        .codeName(codeInfo.getCodeName())
                        .codeGroup(codeInfo.getCodeGroup())
                        .description(codeInfo.getDescription())
                        .status(codeInfo.getStatus())
                        .updatedAt( codeInfo.getUpdatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                        .build() )
                .orElseThrow( () -> new IctkException(trId, AppCode.SNRULE_PROC_ERROR, "Code 정보 "+searchRQB.getCodeId()+ " 없음.") );

        return searchRSB;
    }

    @Override
    // CODE_INFO 추가(생성)/변경(편집) 서비스
    public CodeInfoSaveDTO.CodeInfoSaveRSB saveCodeInfo(String trId, CodeInfoSaveDTO.CodeInfoSaveRQB saveRQB) throws IctkException {
        boolean toUpdate = false;

        CodeInfo findCodeInfo = null;
//        var burnDateStr = StringUtils.getDateNumberStr( saveRQB.getLastBurnDate());
        if(CommonUtils.hasValue(saveRQB.getCodeId())) {
            toUpdate = true;
            findCodeInfo = codeInfoRepository.findCodeInfoByCodeId( saveRQB.getCodeId() )
                    .orElseThrow( () -> new IctkException(trId, AppCode.SNRULE_PROC_ERROR, "Code 정보"+saveRQB.getCodeId()+ " 없음.") );
            findCodeInfo.setCodeName(saveRQB.getCodeName());
            findCodeInfo.setCodeGroup(saveRQB.getCodeGroup());
            findCodeInfo.setDescription(saveRQB.getDescription());
            findCodeInfo.setStatus(saveRQB.getStatus());
            findCodeInfo.setUpdatedAt(LocalDateTime.now());
        }
        CodeInfo savedCodeInfo = codeInfoRepository.save(
                toUpdate ? findCodeInfo : CodeInfo.builder()
                        .codeName(saveRQB.getCodeName())
                        .codeGroup(saveRQB.getCodeGroup())
                        .description(saveRQB.getDescription())
                        .status(saveRQB.getStatus())
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return CodeInfoSaveDTO.CodeInfoSaveRSB.builder()
                .codeId(savedCodeInfo.getCodeId())
                .codeName(savedCodeInfo.getCodeName())
                .build();
    }

    @Override
    // CODE_INFO 삭제 서비스
    public CodeInfoDeleteDTO.CodeInfoDeleteRSB deleteCodeInfo(String trId, CodeInfoDeleteDTO.CodeInfoDeleteRQB deleteRQB) throws IctkException {
        codeInfoRepository.findCodeInfoByCodeId(deleteRQB.getCodeId())
                .orElseThrow(() -> new IctkException(trId, AppCode.SNRULE_PROC_ERROR, "코드 정보" + deleteRQB.getCodeId() + "없음"));

        long dcnt = codeInfoRepository.deleteCodeInfoByCodeId(deleteRQB.getCodeId());

        return CodeInfoDeleteDTO.CodeInfoDeleteRSB.builder()
                .result((dcnt > 0) ? AppConstants.SUCC : AppConstants.FAIL)
                .deleteCnt((int) dcnt)
                .build();

    }
}

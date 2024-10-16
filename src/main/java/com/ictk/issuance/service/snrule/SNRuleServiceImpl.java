package com.ictk.issuance.service.snrule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.common.utils.StringUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.shared.AppDTO;
import com.ictk.issuance.data.dto.snrule.SNRuleDeleteDTO.SNRuleDeleteRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleDeleteDTO.SNRuleDeleteRSB;
import com.ictk.issuance.data.dto.snrule.SNRuleListDTO.SNRuleListRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleListDTO.SNRuleListRSB;
import com.ictk.issuance.data.dto.snrule.SNRuleSaveDTO.SNRuleSaveRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleSaveDTO.SNRuleSaveRSB;
import com.ictk.issuance.data.dto.snrule.SNRuleSearchDTO.SNRuleSearchRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleSearchDTO.SNRuleSearchRSB;
import com.ictk.issuance.data.model.SNRule;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.properties.SNRuleProperties;
import com.ictk.issuance.repository.SNRuleRepository;
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

import static com.ictk.issuance.data.model.QSNRule.sNRule;

@Slf4j
@RequiredArgsConstructor
@Service
public class SNRuleServiceImpl implements SNRuleService {

    private final IssuanceManager issuanceManager;

    private final DBProperties dbProperties;

    private final SNRuleProperties snRuleProperties;

    private final SNRuleRepository snRuleRepository;

    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    // SN_RULE 테이블 생성하기
    public String createSNRuleTable() {
        return issuanceManager.createTable( dbProperties.database(), snRuleProperties.tableName(),
                (database, table) -> {
                    if ( !snRuleRepository.isTableExist(database, table) ) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if ( !snRuleRepository.makeTable(database, table)) {
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
    @Transactional
    // SN_RULE 리스트 조회 서비스
    public SNRuleListRSB querySNRules(String trId, SNRuleListRQB listRQB) throws IctkException {

        // 데이터 헤더 정보 구성
        Map<String, AppDTO.HeaderInfoObj> hdrInfoMap = AppHelper.snRuleHeaderInfoMap();
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
                SNRule.class, "sNRule", hdrInfoMap, listRQB.getSortKeyName(), listRQB.getOrder() );

        // 동적 쿼리 생성
        BooleanBuilder queryConds = new BooleanBuilder();
        if(CommonUtils.hasValue(listRQB.getSnrName()))
            queryConds.and(sNRule.snrName.contains( listRQB.getSnrName() ));
        if(CommonUtils.hasValue(listRQB.getTestCode()))
            queryConds.and(sNRule.testCode.contains( listRQB.getTestCode() ));

        BooleanBuilder filterConds = AppHelper.composeFilterDynamicConditions(
                listRQB.getFilterArrAndOr(),
                listRQB.getFilterArr(),
                (kname) -> {
                    switch(kname) {
                        case "snr_name" -> { return sNRule.snrName; }
                        case "test_code" -> { return sNRule.testCode; }
                    }
                    return null;
                } );
        if(filterConds!=null)
            queryConds.and(filterConds);


        StopWatch timer = new StopWatch();
        timer.start();
        // DB sn_rule 테이블 쿼리
        Tuple2<Long, Page<SNRule>> snRulesPaged = snRuleRepository.getSNRulePageByCondition(queryConds, listRQB.getPageble(), orderSpecifiers);
        timer.stop();
        log.info("Query tooks {} nanos ", (int)Math.round(timer.getTotalTimeNanos()) );

        return SNRuleListRSB.builder()
                .totalCnt( snRulesPaged._1() )
                .curPage( listRQB.getPageble().getPageNumber() )
                .headerInfos( listRQB.isHeaderInfo() ? headerInfos : null )
                .snRuleList( composeSNRuleList(hdrInfoMap, snRulesPaged._2().toList()) )
                .build();

    }

    private List<Map<String,Object>> composeSNRuleList(Map<String, AppDTO.HeaderInfoObj> hdrInfoMap, List<SNRule> snRules ) {
        final List<Map<String,Object>> bodyList = new ArrayList<>();
        if(hdrInfoMap!=null && snRules!=null) {
            AtomicInteger idx = new AtomicInteger(1);
            snRules.forEach( snRule -> {
                Map<String,Object> dataMap = new LinkedHashMap<>();
                dataMap.put("idx", idx.get() );
                hdrInfoMap.forEach( (k,v) -> {
                    switch(v.getKeyName()) {
                        case "snr_id" -> dataMap.put(v.getKeyName(), snRule.getSnrId());
                        case "snr_name" -> dataMap.put(v.getKeyName(), snRule.getSnrName());
                        case "test_code" -> dataMap.put(v.getKeyName(), snRule.getTestCode());
                        case "location" -> dataMap.put(v.getKeyName(), snRule.getLocation());
                        case "today_count" -> dataMap.put(v.getKeyName(), snRule.getTodayCount());
                        case "count_sum" -> dataMap.put(v.getKeyName(), snRule.getCountSum());
                        case "created_at" -> dataMap.put(v.getKeyName(), snRule.getCreatedAt()
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
    public SNRuleSearchRSB infoSNRule(String trId, SNRuleSearchRQB searchRQB) throws IctkException {
        SNRuleSearchRSB searchRSB = snRuleRepository.findSNRuleBySnrId( searchRQB.getSnrId() )
                .map( snRule -> SNRuleSearchRSB.builder()
                        .snrId(snRule.getSnrId())
                        .snrName(snRule.getSnrName())
                        .testCode(snRule.getTestCode())
                        .location(snRule.getLocation())
                        .lastBurnDate(snRule.getLastBurnDate()!=null?
                                snRule.getLastBurnDate().format(DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT)):"")
                        .todayCount(snRule.getTodayCount())
                        .countSum(snRule.getCountSum())
                        .createdAt( snRule.getCreatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                        .build() )
                .orElseThrow( () -> new IctkException(trId, AppCode.SNRULE_PROC_ERROR, "SN규칙 "+searchRQB.getSnrId()+ " 없음.") );

        return searchRSB;
    }

    @Override
    // SN_RULE 추가(생성)/변경(편집) 서비스
    public SNRuleSaveRSB saveSNRule(String trId, SNRuleSaveRQB saveRQB) throws IctkException {
        boolean toUpdate = false;

        SNRule findSNRule = null;
        var burnDateStr = StringUtils.getDateNumberStr( saveRQB.getLastBurnDate());
        if(CommonUtils.hasValue(saveRQB.getSnrId())) {
            toUpdate = true;
            findSNRule = snRuleRepository.findSNRuleBySnrId( saveRQB.getSnrId() )
                    .orElseThrow( () -> new IctkException(trId, AppCode.SNRULE_PROC_ERROR, "SN규칙 "+saveRQB.getSnrId()+ " 없음.") );
            findSNRule.setSnrName(saveRQB.getSnrName());
            findSNRule.setTestCode(saveRQB.getTestCode());
            findSNRule.setLocation(saveRQB.getLocation());
            findSNRule.setLastBurnDate( burnDateStr.length()==AppConstants.DATE_DEFAULT_FMT.length() ?
                    LocalDateTime.parse(burnDateStr, DateTimeFormatter.ofPattern(AppConstants.DATE_DEFAULT_FMT)) : null );
            findSNRule.setTodayCount(saveRQB.getTodayCount());
            findSNRule.setCountSum(saveRQB.getCountSum());
            findSNRule.setUpdatedAt(LocalDateTime.now());
        }
        SNRule savedSNRule = snRuleRepository.save(
                toUpdate ? findSNRule : SNRule.builder()
                        .snrName(saveRQB.getSnrName())
                        .testCode(saveRQB.getTestCode())
                        .location(saveRQB.getLocation())
                        .lastBurnDate( burnDateStr.length()==AppConstants.DATE_DEFAULT_FMT.length() ?
                                LocalDateTime.parse(burnDateStr, DateTimeFormatter.ofPattern(AppConstants.DATE_DEFAULT_FMT)) : null )
                        .todayCount(saveRQB.getTodayCount())
                        .countSum(saveRQB.getCountSum())
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return SNRuleSaveRSB.builder()
                .snrId(savedSNRule.getSnrId())
                .snrName(savedSNRule.getSnrName())
                .build();

    }

    @Override
    // SN_RULE 삭제 서비스
    public SNRuleDeleteRSB deleteSNRule(String trId, SNRuleDeleteRQB deleteRQB) throws IctkException {
        snRuleRepository.findSNRuleBySnrId( deleteRQB.getSnrId() )
                .orElseThrow( () -> new IctkException(trId, AppCode.SNRULE_PROC_ERROR, "SN규칙 "+deleteRQB.getSnrId()+ " 없음.") );

        long dcnt = snRuleRepository.deleteSNRuleBySnrId( deleteRQB.getSnrId() );

        return SNRuleDeleteRSB.builder()
                .result( (dcnt>0) ? AppConstants.SUCC : AppConstants.FAIL )
                .deleteCnt( (int)dcnt)
                .build();
    }


}

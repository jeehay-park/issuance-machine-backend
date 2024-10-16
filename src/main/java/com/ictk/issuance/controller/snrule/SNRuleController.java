package com.ictk.issuance.controller.snrule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.dto.CommonRequest;
import com.ictk.issuance.common.dto.CommonResponse;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.security.SessionInfo;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.snrule.SNRuleDeleteDTO.SNRuleDeleteRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleDeleteDTO.SNRuleDeleteRSB;
import com.ictk.issuance.data.dto.snrule.SNRuleListDTO.SNRuleListRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleListDTO.SNRuleListRSB;
import com.ictk.issuance.data.dto.snrule.SNRuleSaveDTO.SNRuleSaveRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleSaveDTO.SNRuleSaveRSB;
import com.ictk.issuance.data.dto.snrule.SNRuleSearchDTO.SNRuleSearchRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleSearchDTO.SNRuleSearchRSB;
import com.ictk.issuance.service.snrule.SNRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name="시리얼넘버 규칙 서비스", description = "시리얼넘버 규칙 서비스 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ictk/issue/admin/snrule")
@RestController
public class SNRuleController {

    private final SNRuleService snRuleService;

    private final ObjectMapper objectMapper;

    @Operation(summary = "시리얼넘버(SN) 규칙 목록 조회", description = "시리얼넘버규칙 - SNRule 목록 조회 서비스 API")
    @PostMapping("/list")
    public CommonResponse<SNRuleListRSB> snRuleListSearch(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<SNRuleListRQB> snRuleListRequest ) {

        final var listTrId = "500300";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, snRuleListRequest));

        if(!listTrId.equals( snRuleListRequest.getHeader().getTrId()) )
            throw new IctkException(listTrId, AppCode.TRID_INVALID, snRuleListRequest.getHeader().getTrId());

        CommonResponse<SNRuleListRSB> snRuleListResponse =  CommonResponse.ok(listTrId,
                snRuleService.querySNRules(
                        listTrId,
                        snRuleListRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, snRuleListResponse));

        return snRuleListResponse;
    }

    @Operation(summary = "시리얼넘버(SN) 규칙 정보 조회", description = "시리얼넘버규칙 - SNRule 정보 조회 서비스 API")
    @PostMapping("/info")
    public CommonResponse<SNRuleSearchRSB> snRuleInfoSearch(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<SNRuleSearchRQB> snrRequest ) {

        final var infoTrId = "500301";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, snrRequest));

        if(!infoTrId.equals( snrRequest.getHeader().getTrId()) )
            throw new IctkException(infoTrId, AppCode.TRID_INVALID, snrRequest.getHeader().getTrId());

        CommonResponse<SNRuleSearchRSB> snrResponse =  CommonResponse.ok(infoTrId,
                snRuleService.infoSNRule(
                        infoTrId,
                        snrRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, snrResponse));

        return snrResponse;

    }

    @Operation(summary = "시리얼넘버(SN) 규칙 추가(생성)/변경(편집)", description = "시리얼넘버규칙 - SNRule 추가(생성)/변경(편집) 서비스 API")
    // @PostMapping(value = {"/create", "/update"} )
    @PostMapping("/save")
    public CommonResponse<SNRuleSaveRSB> snRuleSave(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<SNRuleSaveRQB> saveRequest ) {

        final var saveTrId = "500302";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, saveRequest));

        if(!saveTrId.equals( saveRequest.getHeader().getTrId()) )
            throw new IctkException(saveTrId, AppCode.TRID_INVALID, saveRequest.getHeader().getTrId());

        CommonResponse<SNRuleSaveRSB> saveResponse =  CommonResponse.ok(saveTrId,
                snRuleService.saveSNRule(
                        saveTrId,
                        saveRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, saveResponse));

        return saveResponse;

    }

    @Operation(summary = "시리얼넘버(SN) 규칙 삭제", description = "시리얼넘버규칙 - SNRule 삭제 서비스 API")
    @PostMapping("/delete" )
    public CommonResponse<SNRuleDeleteRSB> snRuleDelete(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<SNRuleDeleteRQB> snrDelRequest ) {

        final var delTrId = "500303";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, snrDelRequest));

        if(!delTrId.equals( snrDelRequest.getHeader().getTrId()) )
            throw new IctkException(delTrId, AppCode.TRID_INVALID, snrDelRequest.getHeader().getTrId());

        CommonResponse<SNRuleDeleteRSB> snrDelResponse =  CommonResponse.ok(delTrId,
                snRuleService.deleteSNRule(
                        delTrId,
                        snrDelRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, snrDelResponse));

        return snrDelResponse;

    }


}

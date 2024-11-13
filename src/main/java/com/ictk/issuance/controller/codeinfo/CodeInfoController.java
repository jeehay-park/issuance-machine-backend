package com.ictk.issuance.controller.codeinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.dto.CommonRequest;
import com.ictk.issuance.common.dto.CommonResponse;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.security.SessionInfo;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoDeleteDTO;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoListDTO;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoSaveDTO;
import com.ictk.issuance.data.dto.codeinfo.CodeInfoSearchDTO;
import com.ictk.issuance.service.codeinfo.CodeInfoService;
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

@Tag(name="코드 정보 서비스", description = "코드 정보 서비스 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ictk/issue/admin/codeinfo")
@RestController
public class CodeInfoController {

    private final CodeInfoService codeInfoService;

    private final ObjectMapper objectMapper;

    @Operation(summary = "코드 정보 목록 조회", description = "코드 정보 - CodeInfo 목록 조회 서비스 API")
    @PostMapping("/list")
    public CommonResponse<CodeInfoListDTO.CodeInfoListRSB> codeInfoListSearch (
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<CodeInfoListDTO.CodeInfoListRQB> codeInfoListRequest
    ) {
        final var listTrId = "500600";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, codeInfoListRequest));
        if(!listTrId.equals( codeInfoListRequest.getHeader().getTrId()) )
            throw new IctkException(listTrId, AppCode.TRID_INVALID, codeInfoListRequest.getHeader().getTrId());

        CommonResponse<CodeInfoListDTO.CodeInfoListRSB> codeInfoListResponse =  CommonResponse.ok(listTrId,
                codeInfoService.queryCodeInfo(
                        listTrId,
                        codeInfoListRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, codeInfoListResponse));

        return codeInfoListResponse;

    }

    @Operation(summary = "코드 정보 조회", description = "코드 정보 - CodeInfo 서비스 API")
    @PostMapping("/info")
    public CommonResponse<CodeInfoSearchDTO.CodeInfoSearchRSB> snRuleInfoSearch(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<CodeInfoSearchDTO.CodeInfoSearchRQB> codeInfoRequest ) {

        final var infoTrId = "500501";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, codeInfoRequest));

        if(!infoTrId.equals( codeInfoRequest.getHeader().getTrId()) )
            throw new IctkException(infoTrId, AppCode.TRID_INVALID, codeInfoRequest.getHeader().getTrId());

        CommonResponse<CodeInfoSearchDTO.CodeInfoSearchRSB> codeInfoResponse =  CommonResponse.ok(infoTrId,
                codeInfoService.infoCodeInfo(
                        infoTrId,
                        codeInfoRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, codeInfoResponse));

        return codeInfoResponse;

    }

    @Operation(summary = "코드 정보 규칙 추가(생성)/변경(편집)", description = "코드 정보 - 코드 정보 추가(생성)/변경(편집) 서비스 API")
    // @PostMapping(value = {"/create", "/update"} )
    @PostMapping("/save")
    public CommonResponse<CodeInfoSaveDTO.CodeInfoSaveRSB> codeInfoSave(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<CodeInfoSaveDTO.CodeInfoSaveRQB> saveRequest

    ) {

        final var saveTrId = "500602";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, saveRequest));

        if(!saveTrId.equals( saveRequest.getHeader().getTrId()) )
            throw new IctkException(saveTrId, AppCode.TRID_INVALID, saveRequest.getHeader().getTrId());

        CommonResponse<CodeInfoSaveDTO.CodeInfoSaveRSB> saveResponse =  CommonResponse.ok(saveTrId,
                codeInfoService.saveCodeInfo(
                        saveTrId,
                        saveRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, saveResponse));

        return saveResponse;

    }

    @Operation(summary = "코드 정보 삭제", description = "코드 정보 - 코드 정보 삭제 서비스 API")
    @PostMapping("/delete" )
    public CommonResponse<CodeInfoDeleteDTO.CodeInfoDeleteRSB> codeInfoDelete (
    @AuthenticationPrincipal SessionInfo sessionInfo,
    @Valid @RequestBody CommonRequest<CodeInfoDeleteDTO.CodeInfoDeleteRQB> codeInfoDelRequest
            ) {
        final var delTrId = "500603";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, codeInfoDelRequest));

        if(!delTrId.equals( codeInfoDelRequest.getHeader().getTrId()) )
            throw new IctkException(delTrId, AppCode.TRID_INVALID, codeInfoDelRequest.getHeader().getTrId());

        CommonResponse<CodeInfoDeleteDTO.CodeInfoDeleteRSB> snrDelResponse =  CommonResponse.ok(delTrId,
                codeInfoService.deleteCodeInfo(
                        delTrId,
                        codeInfoDelRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, snrDelResponse));

        return snrDelResponse;

    }
}

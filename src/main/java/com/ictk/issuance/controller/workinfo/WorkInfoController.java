package com.ictk.issuance.controller.workinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.dto.CommonRequest;
import com.ictk.issuance.common.dto.CommonResponse;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.security.SessionInfo;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoDeleteDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoListDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoSaveDTO;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoSearchDTO;
import com.ictk.issuance.data.dto.workinfo.WorkInfoDeleteDTO;
import com.ictk.issuance.data.dto.workinfo.WorkInfoListDTO;
import com.ictk.issuance.data.dto.workinfo.WorkInfoSaveDTO;
import com.ictk.issuance.data.dto.workinfo.WorkInfoSearchDTO;
import com.ictk.issuance.service.workinfo.WorkInfoService;
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

@Slf4j
@Tag(name="작업화면", description = "작업화면 서비스 API")
@RequiredArgsConstructor
@RequestMapping("/ictk/issue/admin/work")
@RestController
public class WorkInfoController {

    private final WorkInfoService workInfoService;

    private final ObjectMapper objectMapper;

    @Operation(summary = "발급 작업 조회", description = "발급 작업 정보 - 발급 작업 목록 조회를 위한 API")
    @PostMapping("/list")
    public CommonResponse<WorkInfoListDTO.WorkInfoListRSB> workInfoList(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<WorkInfoListDTO.WorkInfoListRQB> workInfoListRequest
    ) {

        final var listTrId = "500100";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, workInfoListRequest));

        if(!listTrId.equals(workInfoListRequest.getHeader().getTrId())) {
            throw new IctkException(listTrId, AppCode.TRID_INVALID, workInfoListRequest.getHeader().getTrId());
        }

        CommonResponse<WorkInfoListDTO.WorkInfoListRSB> workInfoListResponse
                = CommonResponse.ok(
                        listTrId,
                workInfoService.fetchWorkInfoList(
                        listTrId,
                        workInfoListRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, workInfoListResponse));

        return workInfoListResponse;
    }

    @Operation(summary = "발급작업(단일) 정보 조회", description = "발급 작업 정보 - 발급 작업 (단일) 정보 조회를 위한 API")
    @PostMapping("/info")
    public CommonResponse<WorkInfoSearchDTO.WorkInfoSearchRSB> workInfoSearch(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<WorkInfoSearchDTO.WorkInfoSearchRQB> workInfoSearchRequest
    ) {
        final var infoTrId = "500101";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, workInfoSearchRequest));

        if(!infoTrId.equals(workInfoSearchRequest.getHeader().getTrId())) {
            throw new IctkException(infoTrId, AppCode.TRID_INVALID, workInfoSearchRequest.getHeader().getTrId());
        }

        CommonResponse<WorkInfoSearchDTO.WorkInfoSearchRSB> workInfoSearhResponse
                = CommonResponse.ok(
                infoTrId,
                workInfoService.searchWorkInfo(
                        infoTrId,
                        workInfoSearchRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, workInfoSearhResponse));

        return workInfoSearhResponse;

    }

    @PostMapping("/delete")
    @Operation(summary = "발급 작업 삭제 ", description = "발급 작업 정보 - 발급 작업 삭제를 위한 API")
    public CommonResponse<WorkInfoDeleteDTO.WorkInfoDeleteRSB> workInfoDelete (
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<WorkInfoDeleteDTO.WorkInfoDeleteRQB> workInfoDelRequest

    ) {
        final var delTrId = "500103";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, workInfoDelRequest));

        if(!delTrId.equals(workInfoDelRequest.getHeader().getTrId())) {
            throw new IctkException(delTrId, AppCode.TRID_INVALID, workInfoDelRequest.getHeader().getTrId());
        }

        CommonResponse<WorkInfoDeleteDTO.WorkInfoDeleteRSB> workInfoDelResponse
                = CommonResponse.ok(
                delTrId,
                workInfoService.deleteWorkInfo(
                        delTrId,
                        workInfoDelRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, workInfoDelResponse));

        return workInfoDelResponse;
    }

    @Operation(summary = "발급 작업 생성/변경", description = "발급 작업 정보 - 발급 작업 삭제를 위한 API")
    @PostMapping("/save")
    public CommonResponse<WorkInfoSaveDTO.WorkInfoSaveRSB> workInfoSave(

            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<WorkInfoSaveDTO.WorkInfoSaveRQB> workInfoSaveRequest
    ) {

        final var saveTrId = "500102";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, workInfoSaveRequest));

        if(!saveTrId.equals(workInfoSaveRequest.getHeader().getTrId())) {
            throw new IctkException(saveTrId, AppCode.TRID_INVALID, workInfoSaveRequest.getHeader().getTrId());
        }

        CommonResponse<WorkInfoSaveDTO.WorkInfoSaveRSB> workInfoSaveResponse
                = CommonResponse.ok(
                saveTrId,
                workInfoService.saveWorkInfo(
                        saveTrId,
                        workInfoSaveRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, workInfoSaveResponse));

        return workInfoSaveResponse;
    }

}
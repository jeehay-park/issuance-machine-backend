package com.ictk.issuance.controller.workhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.dto.CommonRequest;
import com.ictk.issuance.common.dto.CommonResponse;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.security.SessionInfo;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.workhandler.WorkHandlerDeleteDTO;
import com.ictk.issuance.data.dto.workhandler.WorkHandlerListDTO;
import com.ictk.issuance.data.dto.workhandler.WorkHandlerSaveDTO;
import com.ictk.issuance.data.dto.workinfo.WorkInfoDeleteDTO;
import com.ictk.issuance.data.dto.workinfo.WorkInfoListDTO;
import com.ictk.issuance.data.dto.workinfo.WorkInfoSaveDTO;
import com.ictk.issuance.service.workhandler.WorkHandlerService;
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
@Tag(name = "발급 작업(work) 핸들러 테이블", description = "발급 작업(work) 핸들러 서비스 API")
@RequiredArgsConstructor
@RequestMapping("/ictk/issue/admin/work/handler")
@RestController
public class WorkHandlerController {

    private final WorkHandlerService workHandlerService;

    private final ObjectMapper objectMapper;

    @Operation(summary = "작업 핸들러 목록 조회", description = "작업화면 - 작업 핸들러 목록 조회를 위한 API")
    @PostMapping("/list")
    public CommonResponse<WorkHandlerListDTO.WorkHandlerListRSB> workHandlerList(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<WorkHandlerListDTO.WorkHandlerListRQB> workInfoListRequest

    ) {
        final var listTrId = "500121";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, workInfoListRequest));

        if (!listTrId.equals(workInfoListRequest.getHeader().getTrId())) {
            throw new IctkException(listTrId, AppCode.TRID_INVALID, workInfoListRequest.getHeader().getTrId());
        }

        CommonResponse<WorkHandlerListDTO.WorkHandlerListRSB> workHandlerListResponse
                = CommonResponse.ok(
                listTrId,
                workHandlerService.fetchWorkHandlerList(
                        listTrId,
                        workInfoListRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, workHandlerListResponse));

        return workHandlerListResponse;
    }

    @PostMapping("/delete")
    @Operation(summary = "작업 핸들러 삭제", description = "작업화면 - 작업 핸들러 삭제를 위한 API")
    public CommonResponse<WorkHandlerDeleteDTO.WorkHandlerDeleteRSB> workHandlerDelete(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<WorkHandlerDeleteDTO.WorkHandlerDeleteRQB> workHandlerDelRequest
    ) {
        final var delTrId = "500123";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, workHandlerDelRequest));

        if (!delTrId.equals(workHandlerDelRequest.getHeader().getTrId())) {
            throw new IctkException(delTrId, AppCode.TRID_INVALID, workHandlerDelRequest.getHeader().getTrId());
        }

        CommonResponse<WorkHandlerDeleteDTO.WorkHandlerDeleteRSB> workHandlerDelResponse
                = CommonResponse.ok(
                delTrId,
                workHandlerService.deleteWorkHandler(
                        delTrId,
                        workHandlerDelRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, workHandlerDelResponse));

        return workHandlerDelResponse;
    }

    @Operation(summary = "작업 핸들러 추가(생성)", description = "작업화면 - 작업 핸들러 추가(생성)을 위한 API")
    @PostMapping("/save")
    public CommonResponse<WorkHandlerSaveDTO.WorkHandlerSaveRSB> workHandlerSave(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<WorkHandlerSaveDTO.WorkHandlerSaveRQB> workHandlerSaveRequest

    ) {

        final var saveTrId = "500122";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, workHandlerSaveRequest));

        if (!saveTrId.equals(workHandlerSaveRequest.getHeader().getTrId())) {
            throw new IctkException(saveTrId, AppCode.TRID_INVALID, workHandlerSaveRequest.getHeader().getTrId());
        }

        CommonResponse<WorkHandlerSaveDTO.WorkHandlerSaveRSB> workHandlerSaveResponse
                = CommonResponse.ok(
                saveTrId,
                workHandlerService.saveWorkHandler(
                        saveTrId,
                        workHandlerSaveRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, workHandlerSaveResponse));

        return workHandlerSaveResponse;
    }
}
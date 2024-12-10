package com.ictk.issuance.controller.programinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.dto.CommonRequest;
import com.ictk.issuance.common.dto.CommonResponse;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.security.SessionInfo;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.config.ConfigIdListDTO;
import com.ictk.issuance.data.dto.programinfo.*;
import com.ictk.issuance.service.programinfo.ProgramInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.cfgxml.internal.CfgXmlAccessServiceInitiator;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name="발급 프로그램", description = "발급 프로그램 서비스 API")
@RequiredArgsConstructor
@RequestMapping("/ictk/issue/admin/program")
@RestController
public class ProgramInfoController {

    private final ProgramInfoService programInfoService;

    private final ObjectMapper objectMapper;

    @Operation(summary = "발급 프로그램 목록 조회", description = "프로그램 정보 - 프로그램 목록 조회를 위한 API")
    @PostMapping("/list")
    public CommonResponse<ProgramInfoListDTO.ProgramInfoListRSB> programInfoList(

            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<ProgramInfoListDTO.ProgramInfoListRQB> programInfoListRequest

            ) {

        final var listTrId = "500200";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, programInfoListRequest));

        if(!listTrId.equals(programInfoListRequest.getHeader().getTrId())) {
            throw new IctkException(listTrId, AppCode.TRID_INVALID, programInfoListRequest.getHeader().getTrId());
        }
        CommonResponse<ProgramInfoListDTO.ProgramInfoListRSB> programInfoListResponse = CommonResponse.ok(
                listTrId,
                programInfoService.fetchProgramList(
                        listTrId,
                        programInfoListRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, programInfoListResponse ));

        return programInfoListResponse;
    }

    @Operation(summary = "프로그램(단일) 정보 조회", description = "프로그램 정보 - 프로그램 (단일) 정보 조회를 위한 API")
    @PostMapping("/info")
    public CommonResponse<ProgramInfoSearchDTO.ProgramInfoSearchRSB> programInfoSearch(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<ProgramInfoSearchDTO.ProgramInfoSearchRQB> programInfoSearchRequest
    ) {
        final var infoTrId = "500201";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, programInfoSearchRequest));

        if(!infoTrId.equals(programInfoSearchRequest.getHeader().getTrId())) {
            throw new IctkException(infoTrId, AppCode.TRID_INVALID, programInfoSearchRequest.getHeader().getTrId());
        }

        CommonResponse<ProgramInfoSearchDTO.ProgramInfoSearchRSB> programInfoSearhResponse
                = CommonResponse.ok(
                        infoTrId,
                programInfoService.searchProgram(
                        infoTrId,
                        programInfoSearchRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, programInfoSearhResponse));

        return programInfoSearhResponse;
    }

    @PostMapping("/delete")
    @Operation(summary = "프로그램 삭제 ", description = "프로그램 정보 - 프로그램 삭제를 위한 API")
    public CommonResponse<ProgramInfoDeleteDTO.ProgramInfoDeleteRSB> programInfoDelete(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<ProgramInfoDeleteDTO.ProgramInfoDeleteRQB> programInfoDelRequest
    ){
        final var delTrId = "500203";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, programInfoDelRequest));

        if(!delTrId.equals(programInfoDelRequest.getHeader().getTrId())) {
            throw new IctkException(delTrId, AppCode.TRID_INVALID, programInfoDelRequest.getHeader().getTrId());
        }

        CommonResponse<ProgramInfoDeleteDTO.ProgramInfoDeleteRSB> programInfoDelResponse
                = CommonResponse.ok(
                        delTrId,
                programInfoService.deleteProgram(
                        delTrId,
                        programInfoDelRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, programInfoDelResponse));

        return programInfoDelResponse;
    }


    @Operation(summary = "프로그램 생성/변경", description = "프로그램 정보 - 프로그램 삭제를 위한 API")
    @PostMapping("/save")
    public CommonResponse<ProgramInfoSaveDTO.ProgramInfoSaveRSB> programInfoSave(

            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<ProgramInfoSaveDTO.ProgramInfoSaveRQB> programSaveRequest
    ) {

        final var saveTrId = "500202";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, programSaveRequest));

        if(!saveTrId.equals(programSaveRequest.getHeader().getTrId())) {
            throw new IctkException(saveTrId, AppCode.TRID_INVALID, programSaveRequest.getHeader().getTrId());
        }

        CommonResponse<ProgramInfoSaveDTO.ProgramInfoSaveRSB> programInfoSaveResponse
                = CommonResponse.ok(
                        saveTrId,
                        programInfoService.saveProgram(
                                saveTrId,
                                programSaveRequest.getBody()
                        )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, programInfoSaveResponse ));

        return programInfoSaveResponse;
    }

    @Operation(summary = "프로그램 ID 목록", description = "프로그램 정보 - 프로그램 Id 목록 조회를 위한 API")
    @PostMapping("/id-list")
    public CommonResponse<ProgramInfoIdListDTO.ProgramInfoIdListRSB> fetchProgramIds(
            @AuthenticationPrincipal  SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<ProgramInfoListDTO> programIdRequest
    ) {
        final var idListTrId = "500204";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, programIdRequest));

        if(!idListTrId.equals(programIdRequest.getHeader().getTrId()))
            throw new IctkException(idListTrId, AppCode.TRID_INVALID, programIdRequest.getHeader().getTrId());

        CommonResponse<ProgramInfoIdListDTO.ProgramInfoIdListRSB> progIdResponse = CommonResponse.ok(
                idListTrId,
                programInfoService.programInfoIdsList(
                        idListTrId
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, progIdResponse));

        return progIdResponse;
    }
}

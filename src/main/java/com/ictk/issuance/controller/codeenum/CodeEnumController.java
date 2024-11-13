package com.ictk.issuance.controller.codeenum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.dto.CommonRequest;
import com.ictk.issuance.common.dto.CommonResponse;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.security.SessionInfo;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.codeenum.CodeEnumDeleteDTO;
import com.ictk.issuance.data.dto.codeenum.CodeEnumSaveDTO;
import com.ictk.issuance.service.codeenum.CodeEnumService;
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

@Tag(name="코드 ENUM 서비스", description = "코드 ENUM 서비스 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ictk/issue/admin/codeenum")
@RestController
public class CodeEnumController {

    private final CodeEnumService codeEnumService;

    private final ObjectMapper objectMapper;
    @Operation(summary = "코드 ENUM 생성", description = "코드 ENUM - 코드 ENUM 생성 서비스 API")
    @PostMapping("/create")
    public CommonResponse<CodeEnumSaveDTO.CodeEnumSaveRSB> codeEnumSave(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<CodeEnumSaveDTO.CodeEnumSaveRQB> codeEnumSaveRequest
    ) {

        final var saveTrId = "500604";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, codeEnumSaveRequest));

        if (!saveTrId.equals(codeEnumSaveRequest.getHeader().getTrId()))
            throw new IctkException(saveTrId, AppCode.TRID_INVALID, codeEnumSaveRequest.getHeader().getTrId());

        CommonResponse<CodeEnumSaveDTO.CodeEnumSaveRSB> codeEnumSaveResponse = CommonResponse.ok(
                saveTrId,
                codeEnumService.saveCodeEnum(
                        saveTrId,
                        codeEnumSaveRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, codeEnumSaveResponse));

        return codeEnumSaveResponse;

    }

    @Operation(summary = "코드 ENUM 삭제", description = "코드 ENUM - 코드 ENUM 삭제 서비스 API")
    @PostMapping("/delete" )
    public CommonResponse<CodeEnumDeleteDTO.CodeEnumDeleteRSB> codeEnumDelete(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<CodeEnumDeleteDTO.CodeEnumDeleteRQB> codeEnumDeleteRequest

    ) {

        final var delTrId = "500603";
        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, codeEnumDeleteRequest));

        if(!delTrId.equals( codeEnumDeleteRequest.getHeader().getTrId()) )
            throw new IctkException(delTrId, AppCode.TRID_INVALID, codeEnumDeleteRequest.getHeader().getTrId());

        CommonResponse<CodeEnumDeleteDTO.CodeEnumDeleteRSB> codeEnumDeleteResponse =  CommonResponse.ok(
                delTrId,
                codeEnumService.deleteCodeEnum(
                        delTrId,
                        codeEnumDeleteRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, codeEnumDeleteResponse));

        return codeEnumDeleteResponse;
    }




};

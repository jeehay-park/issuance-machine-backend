package com.ictk.issuance.controller.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.dto.CommonRequest;
import com.ictk.issuance.common.dto.CommonResponse;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.security.SessionInfo;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.config.ConfigDeleteDTO.ConfigDeleteRQB;
import com.ictk.issuance.data.dto.config.ConfigDeleteDTO.ConfigDeleteRSB;
import com.ictk.issuance.data.dto.config.ConfigIdListDTO;
import com.ictk.issuance.data.dto.config.ConfigListDTO.ConfigListRQB;
import com.ictk.issuance.data.dto.config.ConfigListDTO.ConfigListRSB;
import com.ictk.issuance.data.dto.config.ConfigSaveDTO.ConfigSaveRQB;
import com.ictk.issuance.data.dto.config.ConfigSaveDTO.ConfigSaveRSB;
import com.ictk.issuance.data.dto.config.ConfigSearchDTO.ConfigSearchRQB;
import com.ictk.issuance.data.dto.config.ConfigSearchDTO.ConfigSearchRSB;
import com.ictk.issuance.service.config.ConfigService;
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


@Tag(name="발급 설정 서비스", description = "발급 설정 서비스 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ictk/issue/admin/config")
@RestController
public class ConfigController {

    private final ConfigService configService;

    private final ObjectMapper objectMapper;

    @Operation(summary = "발급설정(프로파일/키발급코드/스크립트) 목록 조회", description = "발급설정 - 프로파일/키발급코드/스크립트 목록 조회 서비스 API")
    @PostMapping("/list")
    public CommonResponse<ConfigListRSB> configListSearch(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<ConfigListRQB> configListRequest ) {

        final var listTrId = "500400";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, configListRequest));

        if(!listTrId.equals( configListRequest.getHeader().getTrId()) )
            throw new IctkException(listTrId, AppCode.TRID_INVALID, configListRequest.getHeader().getTrId());

        CommonResponse<ConfigListRSB> configListResponse =  CommonResponse.ok(listTrId,
                configService.queryConfigs(
                        listTrId,
                        configListRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, configListResponse));

        return configListResponse;
    }


    @Operation(summary = "발급설정(프로파일/키발급코드/스크립트) 정보 조회", description = "발급설정 - 프로파일/키발급코드/스크립트 정보 조회 서비스 API")
    @PostMapping("/info")
    public CommonResponse<ConfigSearchRSB> configInfoSearch(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<ConfigSearchRQB> configRequest ) {

        final var infoTrId = "500401";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, configRequest));

        if(!infoTrId.equals( configRequest.getHeader().getTrId()) )
            throw new IctkException(infoTrId, AppCode.TRID_INVALID, configRequest.getHeader().getTrId());

        CommonResponse<ConfigSearchRSB> configResponse =  CommonResponse.ok(infoTrId,
                configService.infoConfig(
                        infoTrId,
                        configRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, configResponse));

        return configResponse;
    }


    @Operation(summary = "발급설정 (프로파일/키발급코드/스크립트) 추가(생성)/변경(편집)", description = "발급설정 - 프로파일/키발급코드/스크립트 추가(생성)/변경(편집) 서비스 API")
    @PostMapping("/save")
    public CommonResponse<ConfigSaveRSB> configSave(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<ConfigSaveRQB> configSaveRequest ) {

        final var saveTrId = "500402";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, configSaveRequest ));

        if(!saveTrId.equals( configSaveRequest.getHeader().getTrId()) )
            throw new IctkException(saveTrId, AppCode.TRID_INVALID, configSaveRequest.getHeader().getTrId());

        CommonResponse<ConfigSaveRSB> configSaveResponse =  CommonResponse.ok(saveTrId,
                configService.saveConfig(
                        saveTrId,
                        configSaveRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, configSaveResponse));

        return configSaveResponse;

    }


    @Operation(summary = "발급설정 (프로파일/키발급코드/스크립트) 삭제", description = "발급설정 - 프로파일/키발급코드/스크립트 삭제 서비스 API")
    @PostMapping("/delete" )
    public CommonResponse<ConfigDeleteRSB> configDelete(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<ConfigDeleteRQB> configDelRequest ) {

        final var delTrId = "500403";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, configDelRequest));

        if(!delTrId.equals( configDelRequest.getHeader().getTrId()) )
            throw new IctkException(delTrId, AppCode.TRID_INVALID, configDelRequest.getHeader().getTrId());

        CommonResponse<ConfigDeleteRSB> configDelResponse =  CommonResponse.ok(delTrId,
                configService.deleteConfig(
                        delTrId,
                        configDelRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, configDelResponse));

        return configDelResponse;

    }

    @Operation(summary = "발급설정 (프로파일/키발급코드/스크립트) ID 목록", description = "발급설정 - 프로파일/키발급코드/스크립트 ID 목록 조회 API")
    @PostMapping("/id-list")
    public CommonResponse<ConfigIdListDTO.ConfigIdListRSB> fetchConfigIds(
            @AuthenticationPrincipal  SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<ConfigIdListDTO> configIdRequest

    ) {
        final var idListTrId = "500404";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, configIdRequest));

        if(!idListTrId.equals(configIdRequest.getHeader().getTrId()))
            throw new IctkException(idListTrId, AppCode.TRID_INVALID, configIdRequest.getHeader().getTrId());

        CommonResponse<ConfigIdListDTO.ConfigIdListRSB> configIdResponse = CommonResponse.ok(
                idListTrId,
                configService.configIdsList(
                        idListTrId
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, configIdResponse));

        return configIdResponse;
    }
}
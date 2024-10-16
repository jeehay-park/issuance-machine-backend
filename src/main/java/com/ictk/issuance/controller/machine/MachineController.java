package com.ictk.issuance.controller.machine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.dto.CommonRequest;
import com.ictk.issuance.common.dto.CommonResponse;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.security.SessionInfo;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.machine.MachineDeleteDTO.MachineDeleteRQB;
import com.ictk.issuance.data.dto.machine.MachineDeleteDTO.MachineDeleteRSB;
import com.ictk.issuance.data.dto.machine.MachineSaveDTO.MachineSaveRQB;
import com.ictk.issuance.data.dto.machine.MachineSaveDTO.MachineSaveRSB;
import com.ictk.issuance.data.dto.machine.MachineListDTO.MachineListRQB;
import com.ictk.issuance.data.dto.machine.MachineListDTO.MachineListRSB;
import com.ictk.issuance.data.dto.machine.MachineSearchDTO.MachineSearchRQB;
import com.ictk.issuance.data.dto.machine.MachineSearchDTO.MachineSearchRSB;
import com.ictk.issuance.service.machine.MachineService;
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


@Tag(name="발급기계 서비스", description = "발급기계 서비스 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ictk/issue/admin/machine")
@RestController
public class MachineController {

    private final MachineService machineService;

    private final ObjectMapper objectMapper;

    @Operation(summary = "발급기계(Machine) 목록 조회", description = "발급기계 - 발급기계(Machine) 목록 조회 서비스 API")
    @PostMapping("/list")
    public CommonResponse<MachineListRSB> machineListSearch(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<MachineListRQB> mcnListRequest ) {

        final var listTrId = "500500";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, mcnListRequest));

        if(!listTrId.equals( mcnListRequest.getHeader().getTrId()) )
            throw new IctkException(listTrId, AppCode.TRID_INVALID, mcnListRequest.getHeader().getTrId());

        CommonResponse<MachineListRSB> mcnListResponse =  CommonResponse.ok(listTrId,
                machineService.queryMachines(
                        listTrId,
                        mcnListRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, mcnListResponse));

        return mcnListResponse;
    }

    @Operation(summary = "발급기계(Machine) 단일 조회", description = "발급기계 - 발급기계(Machine) 단일 조회 서비스 API")
    @PostMapping("/info")
    public CommonResponse<MachineSearchRSB> machineInfoSearch(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<MachineSearchRQB> mcnRequest ) {

        final var infoTrId = "500501";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, mcnRequest));

        if(!infoTrId.equals( mcnRequest.getHeader().getTrId()) )
            throw new IctkException(infoTrId, AppCode.TRID_INVALID, mcnRequest.getHeader().getTrId());

        CommonResponse<MachineSearchRSB> mcnResponse =  CommonResponse.ok(infoTrId,
                machineService.infoMachine(
                        infoTrId,
                        mcnRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, mcnResponse));

        return mcnResponse;

    }

    @Operation(summary = "발급기계(Machine) 추가(생성)/변경(편집)", description = "발급기계 - 발급기계(Machine) 추가(생성)/변경(편집) 서비스 API")
    // @PostMapping(value = {"/create", "/update"} )
    @PostMapping("/save")
    public CommonResponse<MachineSaveRSB> machineSave(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<MachineSaveRQB> mcnSaveRequest ) {

        final var saveTrId = "500502";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, mcnSaveRequest));

        if(!saveTrId.equals( mcnSaveRequest.getHeader().getTrId()) )
            throw new IctkException(saveTrId, AppCode.TRID_INVALID, mcnSaveRequest.getHeader().getTrId());

        CommonResponse<MachineSaveRSB> mcnSaveResponse =  CommonResponse.ok(saveTrId,
                machineService.saveMachine(
                        saveTrId,
                        mcnSaveRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, mcnSaveResponse));

        return mcnSaveResponse;
    }


    @Operation(summary = "발급기계(Machine) 삭제", description = "발급기계 - 발급기계(Machine) 삭제 서비스 API")
    @PostMapping("/delete" )
    public CommonResponse<MachineDeleteRSB> machineDelete(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<MachineDeleteRQB> mcnDelRequest ) {

        final var delTrId = "500503";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, mcnDelRequest));

        if(!delTrId.equals( mcnDelRequest.getHeader().getTrId()) )
            throw new IctkException(delTrId, AppCode.TRID_INVALID, mcnDelRequest.getHeader().getTrId());

        CommonResponse<MachineDeleteRSB> mcnDelResponse =  CommonResponse.ok(delTrId,
                machineService.deleteMachine(
                        delTrId,
                        mcnDelRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, mcnDelResponse));

        return mcnDelResponse;

    }

}

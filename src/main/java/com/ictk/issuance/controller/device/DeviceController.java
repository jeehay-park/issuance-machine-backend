package com.ictk.issuance.controller.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.dto.CommonRequest;
import com.ictk.issuance.common.dto.CommonResponse;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.security.SessionInfo;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.device.DeviceDeleteDTO.DeviceDeleteRQB;
import com.ictk.issuance.data.dto.device.DeviceDeleteDTO.DeviceDeleteRSB;
import com.ictk.issuance.data.dto.device.DeviceSaveDTO.DeviceSaveRQB;
import com.ictk.issuance.data.dto.device.DeviceSaveDTO.DeviceSaveRSB;
import com.ictk.issuance.data.dto.machine.MachineDeleteDTO;
import com.ictk.issuance.service.device.DeviceService;
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


@Tag(name="디바이스 서비스", description = "디바이스 서비스 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ictk/issue/admin/device")
@RestController
public class DeviceController {

    private final DeviceService deviceService;

    private final ObjectMapper objectMapper;

    @Operation(summary = "디바이스(Device) 추가(생성)/변경(편집)", description = "발급기계 - 디바이스(Device) 추가(생성)/변경(편집) 서비스 API")
    @PostMapping("/save")
    public CommonResponse<DeviceSaveRSB> deviceSave(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<DeviceSaveRQB> dvcSaveRequest ) {

        final var saveTrId = "500504";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, dvcSaveRequest));

        if(!saveTrId.equals( dvcSaveRequest.getHeader().getTrId()) )
            throw new IctkException(saveTrId, AppCode.TRID_INVALID, dvcSaveRequest.getHeader().getTrId());

        CommonResponse<DeviceSaveRSB> dvcSaveResponse =  CommonResponse.ok(saveTrId,
                deviceService.saveDevices(
                        saveTrId,
                        dvcSaveRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, dvcSaveResponse));

        return dvcSaveResponse;

    }

    @Operation(summary = "디바이스(Device) 삭제", description = "발급기계 - 디바이스(Device) 삭제 서비스 API")
    @PostMapping("/delete" )
    public CommonResponse<DeviceDeleteRSB> devicesDelete(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<DeviceDeleteRQB> dvcsDelRequest ) {

        final var delTrId = "500505";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, dvcsDelRequest));

        if(!delTrId.equals( dvcsDelRequest.getHeader().getTrId()) )
            throw new IctkException(delTrId, AppCode.TRID_INVALID, dvcsDelRequest.getHeader().getTrId());

        CommonResponse<DeviceDeleteRSB> dvcsDelResponse =  CommonResponse.ok(delTrId,
                deviceService.deleteDevices(
                        delTrId,
                        dvcsDelRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, dvcsDelResponse));

        return dvcsDelResponse;

    }

}

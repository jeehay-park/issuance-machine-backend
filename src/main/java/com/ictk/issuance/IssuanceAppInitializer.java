package com.ictk.issuance;

import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.service.codeenum.CodeEnumService;
import com.ictk.issuance.service.codeinfo.CodeInfoService;
import com.ictk.issuance.service.config.ConfigService;
import com.ictk.issuance.service.device.DeviceService;
import com.ictk.issuance.service.machine.MachineService;
import com.ictk.issuance.service.snrule.SNRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class IssuanceAppInitializer implements CommandLineRunner {

    @Autowired
    MachineService machineService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    ConfigService configService;

    @Autowired
    SNRuleService snRuleService;

    @Autowired
    CodeInfoService codeInfoService;

    @Autowired
    CodeEnumService codeEnumService;


    @Override
    public void run(String... args) throws Exception {
        log.info("IssuanceAppInitializer Args: " + Arrays.toString(args));

        // 머신 테이블 생성
        String machineRtn = machineService.createMachineTable();
        if(AppConstants.FAIL.equals(machineRtn) ) {
            log.error("***** 프로그램을 종료합니다. !! ");
            return;
        }

        // 디바이스 테이블 생성
        String deviceRtn = deviceService.createDeviceTable();
        if(AppConstants.FAIL.equals(deviceRtn) ) {
            log.error("***** 프로그램을 종료합니다. !! ");
            return;
        }

        // 프로파일/키발급코드/스크립트 설정 테이블 생성
        String profileConfigRtn = configService.createProfileConfigTable();
        if(AppConstants.FAIL.equals(profileConfigRtn) ) {
            log.error("***** 프로그램을 종료합니다. !! ");
            return;
        }
        String keyissueConfigRtn = configService.createKeyissueConfigTable();
        if(AppConstants.FAIL.equals(keyissueConfigRtn) ) {
            log.error("***** 프로그램을 종료합니다. !! ");
            return;
        }
        String scriptConfigRtn = configService.createScriptConfigTable();
        if(AppConstants.FAIL.equals(scriptConfigRtn) ) {
            log.error("***** 프로그램을 종료합니다. !! ");
            return;
        }

        // 시리얼넘버 규칙 테이블 생성
        String snRuleRtn = snRuleService.createSNRuleTable();
        if(AppConstants.FAIL.equals(snRuleRtn) ) {
            log.error("***** 프로그램을 종료합니다. !! ");
            return;
        }

        // 코드 정보 테이블 생성
        String codeInfoRtn = codeInfoService.createCodeInfoTable();
        if(AppConstants.FAIL.equals(codeInfoRtn)) {
            log.error("***** 프로그램을 종료합니다. !! ");
            return;
        }
        // 코드 ENUM 테이블 생성
        String codeEnumRtn = codeEnumService.createCodeEnumTable();
        if(AppConstants.FAIL.equals(codeEnumRtn)) {
            log.error("***** 프로그램을 종료합니다. !! ");
            return;
        }

    }
}

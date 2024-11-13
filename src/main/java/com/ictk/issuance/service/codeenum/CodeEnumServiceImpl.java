package com.ictk.issuance.service.codeenum;

import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.codeenum.CodeEnumDeleteDTO;
import com.ictk.issuance.data.dto.codeenum.CodeEnumSaveDTO;
import com.ictk.issuance.data.model.CodeEnum;
import com.ictk.issuance.data.model.CodeInfo;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.CodeEnumProperties;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.repository.CodeEnumRepository;
import com.ictk.issuance.repository.CodeInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class CodeEnumServiceImpl implements CodeEnumService {

    private final IssuanceManager issuanceManager;

    private final DBProperties dbProperties;

    private final CodeEnumProperties codeEnumProperties;

    private final CodeInfoRepository codeInfoRepository;

    private final CodeEnumRepository codeEnumRepository;

    @Override
    @Transactional
    // CODE_ENUM 테이블 생성하기
    public String createCodeEnumTable() {
        return issuanceManager.createTable(
                dbProperties.database(),
                codeEnumProperties.tableName(),
                (database, table) -> {
                    if(!codeEnumRepository.isTableExist(database, table)) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if(!codeEnumRepository.makeTable(database, table)) {
                            log.error("***** "+table+" 테이블 생성에 실패했습니다.");
                            return "FAIL";
                        } else {
                            return "SUCC";
                        }
                    } else {
                        log.info("----- " + table + " 테이블이 존재합니다. ");
                        return "EXIST";
                    }
                }
        );
    }

    @Override
    @Transactional(rollbackFor = { IctkException.class, Exception.class })
    public CodeEnumSaveDTO.CodeEnumSaveRSB saveCodeEnum(String trId, CodeEnumSaveDTO.CodeEnumSaveRQB codeEnumSaveRQB) throws IctkException {

        // CODE_INFO에서 해당 code_id 존재하는지 확인
        CodeInfo findCodeId = codeInfoRepository.findCodeInfoByCodeId(codeEnumSaveRQB.getCodeId())
                .orElseThrow( () -> new IctkException(trId, AppCode.CODEENUM_PROC_ERROR, "코드 ENUM 정보 없음."));

        AtomicInteger updateCnt = new AtomicInteger(0);

        codeEnumSaveRQB.getEnumList().forEach(codeEnum -> {
            try{

                CodeEnum saveCodeEnum = codeEnumRepository.save(
                        CodeEnum.builder()
                                .codeInfo(findCodeId)
                                .enumValue(codeEnum.getEnumValue())
                                .isMandatory(codeEnum.getIsMandatory())
                                .ip(codeEnum.getIp())
                                .order(codeEnum.getOrder())
                                .description(codeEnum.getDescription())
                                .build()
                );

                log.info("Saved code enum with ID: {}", saveCodeEnum.getCodeInfo());
                if (saveCodeEnum.getCodeInfo() != null)
                    updateCnt.getAndIncrement();

            } catch (Exception e) {
                log.error("error ***** {}", e.getMessage());
                throw new IctkException(trId, AppCode.CODEENUM_PROC_ERROR, "CODE ENUM SAVE 오류.");
            }

        });

        return CodeEnumSaveDTO.CodeEnumSaveRSB.builder()
                .result( updateCnt.get() != 0 ? AppConstants.SUCC : AppConstants.FAIL )
                .updateCnt( updateCnt.get() )
                .build();
    }

    // CODE ENUM 삭제 서비스
    @Override
    @Transactional
    public CodeEnumDeleteDTO.CodeEnumDeleteRSB deleteCodeEnum(String trId, CodeEnumDeleteDTO.CodeEnumDeleteRQB codeEnumDeleteRQB) throws IctkException {

        if(!CommonUtils.hasElements(codeEnumDeleteRQB.getCodeEnumIds()))
            throw new IctkException(trId, AppCode.REQ_BODY_ERROR, "Code Id is empty.");

        var delCodeEnumIds = codeEnumRepository.findAllById(codeEnumDeleteRQB.getCodeEnumIds());
        if( !CommonUtils.hasElements(delCodeEnumIds) || delCodeEnumIds.size()!= codeEnumDeleteRQB.getCodeEnumIds().size())
            throw new IctkException(trId, AppCode.DEVICE_PROC_ERROR, "CODE ENUM ID 확인필요");

        try {
            codeEnumRepository.deleteAllInBatch( delCodeEnumIds);
        } catch (Exception e) {
            log.error("error ***** {}", e.getMessage());
            throw new IctkException(trId, AppCode.CODEENUM_PROC_ERROR, "코드 ENUM 삭제 오류.");
        }


        return CodeEnumDeleteDTO.CodeEnumDeleteRSB.builder()
                .result(AppConstants.SUCC)
                .deleteCnt((delCodeEnumIds.size()))
                .build();
    }
}

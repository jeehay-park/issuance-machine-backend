package com.ictk.issuance.service.codeenum;

import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.codeenum.CodeEnumDTO;
import com.ictk.issuance.data.dto.codeenum.CodeEnumDeleteDTO;
import com.ictk.issuance.data.dto.codeenum.CodeEnumListDTO;
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

import java.util.List;
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
    @Transactional
    public CodeEnumSaveDTO.CodeEnumSaveRSB saveCodeEnum(String trId, CodeEnumSaveDTO.CodeEnumSaveRQB codeEnumSaveRQB) throws IctkException {

        boolean toUpdate = false;

        CodeEnum findCodeEnum = null;

        // CODE_INFO에서 해당 code_id 존재하는지 확인
        CodeInfo findCodeId = codeInfoRepository.findCodeInfoByCodeId(codeEnumSaveRQB.getCodeId())
                .orElseThrow( () -> new IctkException(trId, AppCode.CODEENUM_PROC_ERROR, "코드 ENUM 정보 없음."));

        List<CodeEnumDTO.CodeEnumObj> codeEnums = codeEnumRepository.findAllByCodeId(codeEnumSaveRQB.getCodeId());

        if(!codeEnums.isEmpty()) {
            codeEnumRepository.deleteCodeEnumByCodeId(codeEnumSaveRQB.getCodeId());
        }

        AtomicInteger updateCnt = new AtomicInteger(0);

        codeEnumSaveRQB.getEnumList().forEach(codeEnum -> {
            try{

                CodeEnum saveCodeEnum = codeEnumRepository.save(
                        CodeEnum.builder()
                                .codeId(codeEnumSaveRQB.getCodeId())
                                .enumValue(codeEnum.getEnumValue())
                                .isMandatory(codeEnum.getIsMandatory())
                                .ip(codeEnum.getIp())
                                .order(codeEnum.getOrder())
                                .description(codeEnum.getDescription())
                                .build()
                );

                log.info("Saved code enum with ID: {}", saveCodeEnum.getCodeId());
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
        String codeId = codeEnumDeleteRQB.getCodeId();
        // Attempt to find the CodeEnum by codeId
        // Optional<CodeEnum> codeEnumOpt = codeEnumRepository.findAllByCodeId(codeId);

        // Fetch all records matching the codeId
        List<CodeEnumDTO.CodeEnumObj> codeEnums = codeEnumRepository.findAllByCodeId(codeId);

        // Check if no records exist
        if (codeEnums.isEmpty()) {
            throw new IctkException(trId, AppCode.CODEENUM_PROC_ERROR, "코드 ENUM " + codeId + " 없음");
        }

        // Log the records found
        log.info("Found {} CodeEnum records with codeId {}", codeEnums.size(), codeId);

        // Perform the delete operation
        long deleteCount = codeEnumRepository.deleteCodeEnumByCodeId(codeId);

        if (deleteCount == 0) {
            throw new IctkException(trId, AppCode.CODEENUM_PROC_ERROR, "코드 ENUM " + codeId + " 삭제 실패");
        }

        // Log the number of deleted records
        log.info("Deleted {} CodeEnum records with codeId {}", deleteCount, codeId);

        // Build and return the response
        return CodeEnumDeleteDTO.CodeEnumDeleteRSB.builder()
                .result(AppConstants.SUCC)
                .deleteCnt((int) deleteCount)
                .build();
    }

    @Override
    public CodeEnumListDTO.CodeEnumListRSB findAllByCodeId(String codeId) throws IctkException {
        List<CodeEnumDTO.CodeEnumObj> codeEnumObjList = codeEnumRepository.findAllByCodeId(codeId);
        return new CodeEnumListDTO.CodeEnumListRSB(codeEnumObjList);
    }

}

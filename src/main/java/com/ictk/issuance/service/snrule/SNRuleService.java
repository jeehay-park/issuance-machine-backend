package com.ictk.issuance.service.snrule;

import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.snrule.SNRuleListDTO.SNRuleListRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleListDTO.SNRuleListRSB;
import com.ictk.issuance.data.dto.snrule.SNRuleSearchDTO.SNRuleSearchRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleSearchDTO.SNRuleSearchRSB;
import com.ictk.issuance.data.dto.snrule.SNRuleSaveDTO.SNRuleSaveRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleSaveDTO.SNRuleSaveRSB;
import com.ictk.issuance.data.dto.snrule.SNRuleDeleteDTO.SNRuleDeleteRQB;
import com.ictk.issuance.data.dto.snrule.SNRuleDeleteDTO.SNRuleDeleteRSB;


public interface SNRuleService {

    // SN_RULE 테이블 생성하기
    String createSNRuleTable();

    // SN_RULE 리스트 조회 서비스
    SNRuleListRSB querySNRules(String trId, SNRuleListRQB listRQB) throws IctkException;

    // SN_RULE 정보 조회 서비스
    SNRuleSearchRSB infoSNRule(String trId, SNRuleSearchRQB searchRQB) throws IctkException;

    // SN_RULE 추가(생성)/변경(편집) 서비스
    SNRuleSaveRSB saveSNRule(String trId, SNRuleSaveRQB saveRQB) throws IctkException;

    // SN_RULE 삭제 서비스
    SNRuleDeleteRSB deleteSNRule(String trId, SNRuleDeleteRQB deleteRQB) throws IctkException;


}

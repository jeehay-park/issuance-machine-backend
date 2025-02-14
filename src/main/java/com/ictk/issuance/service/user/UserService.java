package com.ictk.issuance.service.user;

import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.user.UserLoginChallengeDTO;
import com.ictk.issuance.data.dto.user.UserLoginRequest;
import com.ictk.issuance.data.dto.user.UserSignUpDTO;

public interface UserService {

    // 사용자 테이블 생성
    String createUserTable();

    // 사용자 등록
    UserSignUpDTO.UserSignUpRSB signup(String trId,  UserSignUpDTO.UserSignUpRQB userInfo) throws IctkException;

    // 사용자 로그인 challenge
    UserLoginChallengeDTO.UserLoginChallengeRSB loginChallenge(String trId, UserLoginChallengeDTO.UserLoginChallengeRQB loginChallengeInfo) throws IctkException;

    // 사용자 로그인 request
    UserLoginRequest.UserLoginRequestRSB loginRequest(String trId, UserLoginRequest.UserLoginRequestRQB loginRequestInfo) throws IctkException;

}

package com.ictk.issuance.service.user;

import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.data.dto.user.*;

public interface UserService {

    // 사용자 테이블 생성
    String createUserTable();

    // 사용자 등록
    UserSignUpDTO.UserSignUpRSB signup(String trId,  UserSignUpDTO.UserSignUpRQB userInfo) throws IctkException;

    // 사용자 로그인 challenge
    UserLoginChallengeDTO.UserLoginChallengeRSB loginChallenge(String trId, UserLoginChallengeDTO.UserLoginChallengeRQB loginChallengeInfo) throws IctkException;

    // 사용자 로그인 request
    UserLoginRequestDTO.UserLoginRequestRSB loginRequest(String trId, UserLoginRequestDTO.UserLoginRequestRQB loginRequestInfo) throws IctkException;

    // 사용자 목록 조회
    UserListDTO.UserListRSB userList(String trId, UserListDTO.UserListRQB userListRQB) throws IctkException;

    // 사용자 삭제
    UserDeleteDTO.UserDeleteRSB deleteUser(String trId, UserDeleteDTO.UserDeleteRQB userDeleteRQB) throws IctkException;

    // 사용자 변경
    UserEditDTO.UserEditRSB editUser(String trId, UserEditDTO.UserEditRQB userEditRQB) throws IctkException;
}



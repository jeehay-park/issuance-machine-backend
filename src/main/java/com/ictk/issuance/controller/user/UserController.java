package com.ictk.issuance.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.dto.CommonRequest;
import com.ictk.issuance.common.dto.CommonResponse;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.security.SessionInfo;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.programinfo.ProgramInfoListDTO;
import com.ictk.issuance.data.dto.user.UserLoginChallengeDTO;
import com.ictk.issuance.data.dto.user.UserLoginRequest;
import com.ictk.issuance.data.dto.user.UserSignUpDTO;
import com.ictk.issuance.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name="사용자 관리", description = "사용자 관리 서비스 API")
@Validated
@RestController
@RequestMapping("/ictk/issue/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    @Operation(summary = "발급서버 사용자 등록", description = "사용자 - 사용자 등록을 위한 API")
    @PostMapping("/signup")
    public CommonResponse<UserSignUpDTO.UserSignUpRSB> signup(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<UserSignUpDTO.UserSignUpRQB> userSignUpRQBRequest
    ) {

        final var listTrId = "500700";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, userSignUpRQBRequest));

        if(!listTrId.equals(userSignUpRQBRequest.getHeader().getTrId())) {
            throw new IctkException(listTrId, AppCode.TRID_INVALID, userSignUpRQBRequest.getHeader().getTrId());
        }

        CommonResponse<UserSignUpDTO.UserSignUpRSB> userSignUpRSBCommonResponse = CommonResponse.ok(
                listTrId,
                userService.signup(listTrId, userSignUpRQBRequest.getBody())
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, userSignUpRSBCommonResponse));

        return userSignUpRSBCommonResponse;
    }


    @Operation(summary = "발급서버 사용자 로그인", description = "사용자 - 사용자 로그인 challenge API")
    @PostMapping("/login/challenge")
    public CommonResponse<UserLoginChallengeDTO.UserLoginChallengeRSB> loginChallenge(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<UserLoginChallengeDTO.UserLoginChallengeRQB> userLoginChallengeRQBRequest

    ) {
        final var listTrId = "500701";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, userLoginChallengeRQBRequest));

        if(!listTrId.equals(userLoginChallengeRQBRequest.getHeader().getTrId())) {
            throw new IctkException(listTrId, AppCode.TRID_INVALID, userLoginChallengeRQBRequest.getHeader().getTrId());
        }

        CommonResponse<UserLoginChallengeDTO.UserLoginChallengeRSB> userLoginChallengeRSBResponse = CommonResponse.ok(
                listTrId,
                userService.loginChallenge(listTrId, userLoginChallengeRQBRequest.getBody())
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, userLoginChallengeRSBResponse));

        return userLoginChallengeRSBResponse;
    }

    @Operation(summary = "발급서버 사용자 로그인", description = "사용자 - 사용자 로그인 request API")
    @PostMapping("/login/request")
    public CommonResponse<UserLoginRequest.UserLoginRequestRSB> loginRequest(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<UserLoginRequest.UserLoginRequestRQB> userLoginRequestRQBRequest
    ) {
        final var listTrId = "500702";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, userLoginRequestRQBRequest));

        if(!listTrId.equals(userLoginRequestRQBRequest.getHeader().getTrId())) {
            throw new IctkException(listTrId, AppCode.TRID_INVALID, userLoginRequestRQBRequest.getHeader().getTrId());
        }

        CommonResponse<UserLoginRequest.UserLoginRequestRSB> userLoginRequestRSBCommonResponse = CommonResponse.ok(
                listTrId,
                userService.loginRequest(listTrId, userLoginRequestRQBRequest.getBody())
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, userLoginRequestRSBCommonResponse));

        return userLoginRequestRSBCommonResponse;
    }
}

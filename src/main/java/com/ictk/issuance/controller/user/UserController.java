package com.ictk.issuance.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.dto.CommonRequest;
import com.ictk.issuance.common.dto.CommonResponse;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.security.SessionInfo;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.user.*;
import com.ictk.issuance.data.dto.workhandler.WorkHandlerListDTO;
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

    @Operation(summary = "발급서버 사용자 등록", description = "사용자 등록을 위한 API")
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


    @Operation(summary = "발급서버 사용자 로그인", description = "사용자 로그인 challenge API")
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

    @Operation(summary = "발급서버 사용자 로그인", description = "사용자 로그인 request API")
    @PostMapping("/login/request")
    public CommonResponse<UserLoginRequestDTO.UserLoginRequestRSB> loginRequest(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<UserLoginRequestDTO.UserLoginRequestRQB> userLoginRequestRQBRequest
    ) {
        final var listTrId = "500702";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, userLoginRequestRQBRequest));

        if(!listTrId.equals(userLoginRequestRQBRequest.getHeader().getTrId())) {
            throw new IctkException(listTrId, AppCode.TRID_INVALID, userLoginRequestRQBRequest.getHeader().getTrId());
        }

        CommonResponse<UserLoginRequestDTO.UserLoginRequestRSB> userLoginRequestRSBCommonResponse = CommonResponse.ok(
                listTrId,
                userService.loginRequest(listTrId, userLoginRequestRQBRequest.getBody())
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, userLoginRequestRSBCommonResponse));

        return userLoginRequestRSBCommonResponse;
    }

    @Operation(summary = "사용자 목록 조회", description = "사용자 목록 조회를 위한 API")
    @PostMapping("/list")
    public CommonResponse<UserListDTO.UserListRSB> userList(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<UserListDTO.UserListRQB> userListRequest

    ) {
        final var listTrId = "500703";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, userListRequest));

        if (!listTrId.equals(userListRequest.getHeader().getTrId())) {
            throw new IctkException(listTrId, AppCode.TRID_INVALID, userListRequest.getHeader().getTrId());
        }

        CommonResponse<UserListDTO.UserListRSB> userListResponse
                = CommonResponse.ok(
                listTrId,

                userService.userList(
                        listTrId,
                        userListRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, userListResponse));

        return userListResponse;
    }

    @Operation(summary = "사용자 정보 변경", description = "사용자 정보 변경을 위한 API")
    @PostMapping("/edit")
    public CommonResponse<UserEditDTO.UserEditRSB> editUser(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<UserEditDTO.UserEditRQB> userEditRequest
    ) {
        final var listTrId = "500705";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, userEditRequest));

        if (!listTrId.equals(userEditRequest.getHeader().getTrId())) {
            throw new IctkException(listTrId, AppCode.TRID_INVALID, userEditRequest.getHeader().getTrId());
        }

        CommonResponse<UserEditDTO.UserEditRSB> userEditResponse
                = CommonResponse.ok(
                listTrId,

                userService.editUser(
                        listTrId,
                        userEditRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, userEditResponse));

        return userEditResponse;
    }

    @Operation(summary = "사용자 정보 삭제", description = "사용자 정보 삭제를 위한 API")
    @PostMapping("/delete")
    public CommonResponse<UserDeleteDTO.UserDeleteRSB> deleteUser(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<UserDeleteDTO.UserDeleteRQB> userDeleteRequest
    ) {
        final var listTrId = "500704";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, userDeleteRequest));

        if (!listTrId.equals(userDeleteRequest.getHeader().getTrId())) {
            throw new IctkException(listTrId, AppCode.TRID_INVALID, userDeleteRequest.getHeader().getTrId());
        }

        CommonResponse<UserDeleteDTO.UserDeleteRSB> userDeleteResponse
                = CommonResponse.ok(
                listTrId,

                userService.deleteUser(
                        listTrId,
                        userDeleteRequest.getBody()
                )
        );

        log.debug("{}", CommonUtils.toJson(objectMapper, userDeleteResponse));

        return userDeleteResponse;
    }
}

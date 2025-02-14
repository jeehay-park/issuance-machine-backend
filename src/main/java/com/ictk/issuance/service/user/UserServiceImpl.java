package com.ictk.issuance.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.data.dto.user.UserLoginChallengeDTO;
import com.ictk.issuance.data.dto.user.UserLoginRequest;
import com.ictk.issuance.data.dto.user.UserSignUpDTO;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.properties.UserProperties;
import com.ictk.issuance.repository.UserRepository;
import com.ictk.issuance.utils.JwtUtil;
import jakarta.transaction.Transactional;
import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.ictk.issuance.data.model.User;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final IssuanceManager issuanceManager;

    private final DBProperties dbProperties;

    private final UserProperties userProperties;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    private final JwtUtil jwtUtil;

    @Override
    @Transactional
// 사용자 테이블 생성
    public String createUserTable() {
        return issuanceManager.createTable(dbProperties.database(), userProperties.tableName(),
                (database, table) -> {
                    if (!userRepository.isTableExist(database, table)) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if (!userRepository.makeTable(database, table)) {
                            log.error("***** " + table + " 테이블 생성에 실패했습니다.");
                            return "FAIL";
                        } else
                            return "SUCC";
                    } else {
                        log.info("----- " + table + " 테이블이 존재합니다. ");
                        return "EXIST";
                    }
                });
    }

    @Override
    public UserSignUpDTO.UserSignUpRSB signup(String trId, UserSignUpDTO.UserSignUpRQB userInfo) throws IctkException {

        if (userInfo.getName() == null || userInfo.getEmail() == null || userInfo.getPassword() == null) {
            throw new IctkException(trId, AppCode.USER_SIGNUP_ERROR, "All fields must be provided.");
        }

        if (userRepository.existsByUserId(userInfo.getUserId()) || userRepository.existsByEmail(userInfo.getEmail())) {
            throw new IctkException(trId, AppCode.USER_SIGNUP_ERROR, "Username or Email already exists.");
        }

        String passSalt = CommonUtils.generateSalt(16);  // Generate a 16-byte salt
        String passwordHash = CommonUtils.sha512(userInfo.getPassword() + passSalt);  // Concatenate password and salt

        // ✅ Create User entity using User.builder() (Not userRepository.builder())
        User user = User.builder()
                .userId(userInfo.getUserId())
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .passwordHash(passwordHash)
                .passSalt(passSalt)
                .createdAt(LocalDateTime.now())
                .build();

        // ✅ Save the user entity to the database
        userRepository.save(user);

        // ✅ Return response using UserSignUpDTO.UserSignUpRSB.builder()
        return UserSignUpDTO.UserSignUpRSB.builder()
                .userId(userInfo.getUserId())
                .build();
    }

    @Override
    public UserLoginChallengeDTO.UserLoginChallengeRSB loginChallenge(String trId, UserLoginChallengeDTO.UserLoginChallengeRQB loginChallengeInfo) throws IctkException {

        if (loginChallengeInfo.getUserId() == null) {
            throw new IctkException(trId, AppCode.USER_LOGIN_CHALLENGE_ERROR, "All fields must be provided.");
        }

        if (!userRepository.existsByUserId(loginChallengeInfo.getUserId())) {
            throw new IctkException(trId, AppCode.USER_LOGIN_CHALLENGE_ERROR, "UserId doesn't exist!");
        }

        final String salt = userRepository.findPassSaltByUserId(loginChallengeInfo.getUserId());

        String uuid = java.util.UUID.randomUUID().toString();

        return UserLoginChallengeDTO.UserLoginChallengeRSB.builder()
                .salt(salt)
                .uuid(uuid)
                .build();
    }

    @Override
    public UserLoginRequest.UserLoginRequestRSB loginRequest(String trId, UserLoginRequest.UserLoginRequestRQB loginRequestInfo) throws IctkException {

        if (loginRequestInfo.getUserId() == null | loginRequestInfo.getUuid() == null | loginRequestInfo.getPasswordHash() == null) {
            throw new IctkException(trId, AppCode.USER_LOGIN_REQUEST_ERROR, "All fields must be provided.");
        }

        if (!userRepository.existsByUserId(loginRequestInfo.getUserId())) {
            throw new IctkException(trId, AppCode.USER_LOGIN_REQUEST_ERROR, "UserId doesn't exist!");
        }

        // check the password!
        String user_passwordHash = userRepository.findPasswordHashByUserId(loginRequestInfo.getUserId());
        String passwordHash = CommonUtils.sha512(loginRequestInfo.getUuid() + user_passwordHash);

        if(loginRequestInfo.getPasswordHash().equals(passwordHash)) {

        String sessionId = java.util.UUID.randomUUID().toString();
        String sessionKey = java.util.UUID.randomUUID().toString();
        String accessToken = jwtUtil.generateToken(loginRequestInfo.getUserId(), sessionId);

        long currentTimeInSeconds = System.currentTimeMillis() / 1000;  // Get current time in seconds
        long expirationTimeInSeconds = currentTimeInSeconds + 3600;  // Add 3600 seconds (1 hour)

        User user = userRepository.findByUserId(loginRequestInfo.getUserId());

        // Create token with the correct expiration time
        UserLoginRequest.Token token = UserLoginRequest.Token.builder()
                .accessToken(accessToken)
                .expiry(3600)
                .expirationTime(currentTimeInSeconds)
                .build();

        UserLoginRequest.UserInfo userInfo = UserLoginRequest.UserInfo.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        return UserLoginRequest.UserLoginRequestRSB.builder()
                .sessionId(sessionId)
                .token(token)
                .sessionKey(sessionKey)
                .userInfo(userInfo)
                .build();

        } else {
            throw new IctkException(trId, AppCode.USER_LOGIN_REQUEST_ERROR, "Login Info is not correct");
        }
    }
}

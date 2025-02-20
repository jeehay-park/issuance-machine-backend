package com.ictk.issuance.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import com.ictk.issuance.data.dto.shared.AppDTO;
import com.ictk.issuance.data.dto.user.*;
import com.ictk.issuance.manager.IssuanceManager;
import com.ictk.issuance.properties.DBProperties;
import com.ictk.issuance.properties.UserProperties;
import com.ictk.issuance.repository.UserRepository;
import com.ictk.issuance.utils.AppHelper;
import com.ictk.issuance.utils.JwtUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import io.vavr.Tuple2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.ictk.issuance.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ictk.issuance.data.model.QUser.user;
//import static com.sun.tools.javac.util.Constants.format;


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
                .updatedAt(LocalDateTime.now())
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
    public UserLoginRequestDTO.UserLoginRequestRSB loginRequest(String trId, UserLoginRequestDTO.UserLoginRequestRQB loginRequestInfo) throws IctkException {

        if (loginRequestInfo.getUserId() == null | loginRequestInfo.getUuid() == null | loginRequestInfo.getPasswordHash() == null) {
            throw new IctkException(trId, AppCode.USER_LOGIN_REQUEST_ERROR, "All fields must be provided.");
        }

        if (!userRepository.existsByUserId(loginRequestInfo.getUserId())) {
            throw new IctkException(trId, AppCode.USER_LOGIN_REQUEST_ERROR, "UserId doesn't exist!");
        }

        // check the password!
        String user_passwordHash = userRepository.findPasswordHashByUserId(loginRequestInfo.getUserId());
        String passwordHash = CommonUtils.sha512(loginRequestInfo.getUuid() + user_passwordHash);

        if (loginRequestInfo.getPasswordHash().equals(passwordHash)) {

            String sessionId = java.util.UUID.randomUUID().toString();
            String sessionKey = java.util.UUID.randomUUID().toString();
            String accessToken = jwtUtil.generateToken(loginRequestInfo.getUserId(), sessionId);

            long currentTimeInSeconds = System.currentTimeMillis() / 1000;  // Get current time in seconds
            long expirationTimeInSeconds = currentTimeInSeconds + 3600;  // Add 3600 seconds (1 hour)

            User user = userRepository.findByUserId(loginRequestInfo.getUserId())
                    .orElseThrow(() -> new IctkException(trId, AppCode.USER_LOGIN_REQUEST_ERROR, "User not found"));;

            // Create token with the correct expiration time
            UserLoginRequestDTO.Token token = UserLoginRequestDTO.Token.builder()
                    .accessToken(accessToken)
                    .expiry(3600)
                    .expirationTime(currentTimeInSeconds)
                    .build();

            UserLoginRequestDTO.UserInfo userInfo = UserLoginRequestDTO.UserInfo.builder()
                    .userId(user.getUserId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();

            return UserLoginRequestDTO.UserLoginRequestRSB.builder()
                    .sessionId(sessionId)
                    .token(token)
                    .sessionKey(sessionKey)
                    .userInfo(userInfo)
                    .build();
        } else {
            throw new IctkException(trId, AppCode.USER_LOGIN_REQUEST_ERROR, "Login Info is not correct");
        }
    }

    @Override
    @Transactional
    // If an exception occurs, the transaction won't roll back unless you manually handle it.
    // If an exception occurs mid-way through the method (e.g., in userRepository.getUserPageByCondition()),
    // partial database operations could still be committed, leading to data inconsistency.
    public UserListDTO.UserListRSB userList(String trId, UserListDTO.UserListRQB userListRQB) throws IctkException {

        // 데이터 헤더 정보 구성
        Map<String, AppDTO.HeaderInfoObj> hdrInfoMap = AppHelper.userHeaderInfoMap();
        final List<AppDTO.HeaderInfoObj> headerInfos = new ArrayList<AppDTO.HeaderInfoObj>();
        hdrInfoMap.forEach((k, v) -> {
            headerInfos.add(v);
        });

        // filter가 있는 경우에 기존 filterArrAndOr, filterArr는 무시되고 filter값이 우선시 된다.
        if (CommonUtils.hasValue(userListRQB.getFilter())) {
            var applied = AppHelper.applyFilterToRequestRQB(hdrInfoMap, userListRQB.getFilter());
            userListRQB.setFilterArrAndOr(applied._1());
            userListRQB.setFilterArr(applied._2());
            log.debug("Filter checked! {} {}", userListRQB.getFilterArrAndOr(), CommonUtils.toJson(objectMapper, userListRQB.getFilterArr()));
        }

        // 동적 소팅 생성
        List<OrderSpecifier> orderSpecifiers = AppHelper.getRequestRQBOrderSpecifiers(
                User.class, "user", hdrInfoMap, userListRQB.getSortKeyName(), userListRQB.getOrder());

        // 동적 쿼리 생성
        BooleanBuilder queryConds = new BooleanBuilder();
        if (CommonUtils.hasValue(userListRQB.getFilter()))
            queryConds.and(user.userId.contains(userListRQB.getFilter()));

        BooleanBuilder filterConds = AppHelper.composeFilterDynamicConditions(
                userListRQB.getFilterArrAndOr(),
                userListRQB.getFilterArr(),
                (kname) -> {
                    switch (kname) {
                        case "user_id" -> {
                            return user.userId;
                        }
                    }
                    return null;
                });

        if (filterConds != null)
            queryConds.and(filterConds);

        StopWatch timer = new StopWatch();
        timer.start();

        // DB user 테이블 쿼리
        Tuple2<Long, Page<User>> userPaged = userRepository.getUserPageByCondition(queryConds, userListRQB.getPageble(), orderSpecifiers);
        timer.stop();
        log.info("Query tooks {} nanos ", (int) Math.round(timer.getTotalTimeNanos()));

        return UserListDTO.UserListRSB.builder()
                .totalCnt(userPaged._1())
                .curPage(userListRQB.getPageble().getPageNumber())
                .headerInfos(userListRQB.isHeaderInfo() ? headerInfos : null)
                .userList(composeUserList(hdrInfoMap, userPaged._2().toList()))
                .build();
    }

    // 사용자 정보 리스트 res body
    private List<Map<String, Object>> composeUserList(Map<String, AppDTO.HeaderInfoObj> hdrInfoMap, List<User> user) {

        final List<Map<String, Object>> bodyList = new ArrayList<>();
        if (hdrInfoMap != null && user != null) {
            AtomicInteger idx = new AtomicInteger(1);
            user.forEach(userItem -> {
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("idx", idx.get());
                hdrInfoMap.forEach((k, v) -> {
                    switch (v.getKeyName()) {
                        case "user_id" -> dataMap.put(v.getKeyName(), userItem.getUserId());
                        case "name" -> dataMap.put(v.getKeyName(), userItem.getName());
                        case "email" -> dataMap.put(v.getKeyName(), userItem.getEmail());
                        case "updated_at" -> dataMap.put(v.getKeyName(), userItem.getUpdatedAt()
                                .format(DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT)));
                        case "created_at" -> dataMap.put(v.getKeyName(), userItem.getCreatedAt()
                                .format(DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT)));
                        default -> {}
                    }
                });
                bodyList.add(dataMap);
                idx.getAndIncrement();
            });
        }
        return bodyList;
    }

    // 사용자 삭제 서비스
    @Override
    public UserDeleteDTO.UserDeleteRSB deleteUser(String trId, UserDeleteDTO.UserDeleteRQB userDeleteRQB) throws IctkException {

        userRepository.findByUserId(userDeleteRQB.getUserId())
                .orElseThrow(() -> new IctkException(trId, AppCode.USER_DELETE_ERROR, "사용자 정보" + userDeleteRQB.getUserId() + "없음"));

        long dcnt = userRepository.deleteUserByUserId(userDeleteRQB.getUserId());

        return UserDeleteDTO.UserDeleteRSB.builder()
                .result((dcnt > 0) ? AppConstants.SUCC : AppConstants.FAIL)
                .deleteCnt((int) dcnt)
                .build();
    }

    // 사용자 변경 서비스
    @Override
    @Transactional
    public UserEditDTO.UserEditRSB editUser(String trId, UserEditDTO.UserEditRQB userEditRQB) throws IctkException {

        if (!CommonUtils.hasValue(userEditRQB.getUserId())) {
            throw new IctkException(trId, AppCode.USER_EDIT_ERROR, "User ID is required.");
        }

        // Find the existing user by userId
        User existingUser = userRepository.findByUserId(userEditRQB.getUserId())
                .orElseThrow(() -> new IctkException(trId, AppCode.USER_EDIT_ERROR, "사용자 정보 " + userEditRQB.getUserId() + " 없음."));

        if(userEditRQB.getPassword() != null && !userEditRQB.getPassword().isEmpty()) {
            String passSalt = CommonUtils.generateSalt(16);  // Generate a 16-byte salt
            String passwordHash = CommonUtils.sha512(userEditRQB.getPassword() + passSalt);
            existingUser.setPassSalt(passSalt);
            existingUser.setPasswordHash(passwordHash);
        }

        if(userEditRQB.getEmail() != null && !userEditRQB.getEmail().isEmpty()) {
            existingUser.setEmail(userEditRQB.getEmail());
        }

        if(userEditRQB.getName() != null && !userEditRQB.getName().isEmpty()) {
            existingUser.setName(userEditRQB.getName());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());

        // Save the updated user back to the repository
        User editedUser = userRepository.save(existingUser);

        // Return the response DTO
        return UserEditDTO.UserEditRSB.builder()
                .userId(editedUser.getUserId())
                .build();
    }
}

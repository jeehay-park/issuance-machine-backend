package com.ictk.issuance.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.common.io.OS;
import com.ictk.issuance.common.io.ProcessOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
public class CommonUtils {

    public static String toJson(ObjectMapper objectMapper, Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("error ***** {}", e.getMessage());
            return "";
        }
    }

    public static boolean hasValue(String strObj) {
        return hasStringValue(strObj);
    }

    public static boolean hasStringValue(String strObj) {
        if (strObj != null && strObj.length()>0)
            return true;
        return false;
    }

    public static boolean hasValues(String strObj1, String strObj2) {
        if(strObj1 == null || strObj1.length() == 0 || strObj2 == null || strObj2.length() == 0) {
            return  true;
        } else {
            return false;
        }
    }


    public static <T> boolean hasElements(List<T> list) {
        return list != null && !list.isEmpty();
    }

    public static <T extends Enum<T>> T getEnum(final Class<T> enumClass, final String value) {
        return Enum.valueOf(enumClass, value);
    }


    /*
     * Apache commons exec 라이브러리 활용. - command 실행.
     *****************************************************************************************
     * 주의사항 : 1) 명령의 나열은 처리할 수 없음.      Ex : cd downloads; ls -la
     *            2) shell 인자등의 처리는 할 수 없음.  Ex : $1, $2등이 들어가는경우.
     *               이 경우에는 쉘 스크립트를 작성하고 그걸 호출하는 식으로 해결.
     *****************************************************************************************
     */
    public static String executeCommandLine(final String commandLine) throws TimeoutException {
        return executeCommandLine(commandLine, 5000L);
    }

    public static String executeCommandLine(final String commandLine, final long timeout) throws TimeoutException {

        Executor executor = new DefaultExecutor();
        executor.setExitValue(0);
        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
        executor.setWatchdog(watchdog);

        ProcessOutputStream outAndErr = new ProcessOutputStream(OS.getOSType());
        try {
            PumpStreamHandler streamHandler = new PumpStreamHandler(outAndErr);

            executor.setStreamHandler(streamHandler);
            executor.execute(CommandLine.parse(commandLine));

        } catch ( IOException e ) {
            if (watchdog.killedProcess()) {
                throw new TimeoutException("Timed out while executing commandLine : '" + commandLine + "'");
            }
        }
        return outAndErr.getOutput();
    }

    //The sha512 method works by applying the SHA-512 cryptographic hash function
    // to an input string and returning the result in a readable hexadecimal format.
    public static String sha512(String input) {
        try {
            // MessageDigest is a built-in Java class for performing cryptographic hashes.
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // converts the input string into a sequence of bytes using UTF-8 encoding
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            // converts the accumulated hexadecimal values into a string and returns it.
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 algorithm not found", e);
        }
    }

    public static String generateSalt(int length) {

//        SecureRandom random = new SecureRandom();  // Produces random bytes (not formatted like a UUID).
//        byte[] salt = new byte[length];
//        random.nextBytes(salt);
//        return Base64.getEncoder().encodeToString(salt);

        //  UUID.randomUUID() always returns a fixed-length (36 characters) string
        return UUID.randomUUID().toString();
    }

}

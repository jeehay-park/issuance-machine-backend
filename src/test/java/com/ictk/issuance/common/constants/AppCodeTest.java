package com.ictk.issuance.common.constants;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class AppCodeTest {

    @Test
    public void AppCodeTest() {
        log.info("{} {} {}", AppCode.SUCCESS.code(),AppCode.SUCCESS.message(), AppCode.SUCCESS.enmessage() );

        log.info("{} {} {}", AppCode.REQ_HEADER_ERROR.code(),AppCode.REQ_HEADER_ERROR.message(), AppCode.REQ_HEADER_ERROR.enmessage() );
    }

}

package com.ictk.issuance;

import com.ictk.issuance.websocket.WebSocketConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@MockBean(WebSocketConfig.class)  // Mock WebSocketConfig if not needed during tests
class IssuanceMachineServerApplicationTests {

	@Test
	void contextLoads() {
	}

}

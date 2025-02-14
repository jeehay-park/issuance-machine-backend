package com.ictk.issuance.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictk.issuance.data.dto.workstatus.WebSocketRequestDTO;
import com.ictk.issuance.data.dto.workstatus.WebSocketResponseDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebSocketService {
    private final ObjectMapper objectMapper;

    public WebSocketService() {
        this.objectMapper = new ObjectMapper();
    }

    public WebSocketResponseDTO.WebSocketResponse processRequest(WebSocketRequestDTO.WebSocketRequestHeader header,
                                                                 WebSocketRequestDTO.WebSocketRequestBody body) throws IOException {

        // Print out request header and body
        System.out.println("Request Header: " + header);
        System.out.println("Request Body: " + body);

        // Use ClassPathResource to load the file from the resources folder
        Resource resource = new ClassPathResource("websocketdata.json"); // device 실제 상태 정보를 받아와야 함
        System.out.println("resource: " + resource);

        System.out.println("body.getWorkId(): " + body.getWorkId());

        // Parse the JSON file into WebSocketResponseDTO.WebSocketResponse
        WebSocketResponseDTO.WebSocketResponseBody parsedBody = objectMapper.readValue(resource.getInputStream(), WebSocketResponseDTO.WebSocketResponseBody.class);
//        System.out.println("responseData: " + responseData);

         // This is where you can call getBody()
        System.out.println("Parsed Body - startedAt: " + parsedBody.getStartedAt());

        WebSocketResponseDTO.WebSocketResponseHeader responseHeader = WebSocketResponseDTO.WebSocketResponseHeader.builder()
                .trId("500111")
                .type(header.getType())
                .rtnCode("00000")
                .rtnMessage("Success")
                .build();

        System.out.println("responseHeader: " + responseHeader);

        WebSocketResponseDTO.WebSocketResponseBody responseBody = WebSocketResponseDTO.WebSocketResponseBody.builder()
                .workId(body.getWorkId())
                .startedAt(parsedBody.getStartedAt()) // Assuming work starts now
                .completedExpAt(parsedBody.getCompletedExpAt()) // Calculate based on your logic
                .remainedTime(parsedBody.getRemainedTime()) // Get remainedTime from parsed JSON data
                .targetQnty(parsedBody.getTargetQnty()) // Get targetQnty from parsed JSON data
                .resourceInfo(parsedBody.getResourceInfo())
                .completedQnty(parsedBody.getCompletedQnty()) // Get completedQnty from parsed JSON data
                .remainedQnty(parsedBody.getRemainedQnty()) // Get remainedQnty from parsed JSON data
                .failedQnty(parsedBody.getFailedQnty()) // Get failedQnty from parsed JSON data
                .sampleQnty(parsedBody.getSampleQnty()) // Get sampleQnty from parsed JSON data
                .workStatus(parsedBody.getWorkStatus()) // Get workStatus from parsed JSON data
                .deviceHandlers(parsedBody.getDeviceHandlers()) // Get deviceHandlers from parsed JSON data
                .build();

        return WebSocketResponseDTO.WebSocketResponse.builder()
                .header(responseHeader)
                .body(responseBody)
                .build();
    }

    private String getCurrentTime() {
        // Example logic to return current time
        return "2025-01-22T10:00:00Z";
    }

    private String calculateExpectedCompletionTime() {
        // Example logic to calculate expected completion time
        return "2025-01-22T12:00:00Z";
    }
}
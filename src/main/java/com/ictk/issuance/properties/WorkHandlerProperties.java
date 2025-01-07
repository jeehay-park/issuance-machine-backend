package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.work-handler")
public record WorkHandlerProperties(
        String tableName
) {
}

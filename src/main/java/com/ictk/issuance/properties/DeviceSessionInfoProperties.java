package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.session-info")
public record DeviceSessionInfoProperties(
        String tableName
) {
}

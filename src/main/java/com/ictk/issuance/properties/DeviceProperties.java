package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.device")
public record DeviceProperties (
        String tableName
) {
}

package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.user")
public record UserProperties(
        String tableName
) {
}

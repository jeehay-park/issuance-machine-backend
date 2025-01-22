package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.work-detail")
public record WorkDetailProperties(
        String tableName
) {
}

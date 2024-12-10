package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.work-info")
public record WorkInfoProperties(
        String tableName
) {

}

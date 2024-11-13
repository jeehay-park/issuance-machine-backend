package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.code-info")
public record CodeInfoProperties(
        String tableName
) {

}

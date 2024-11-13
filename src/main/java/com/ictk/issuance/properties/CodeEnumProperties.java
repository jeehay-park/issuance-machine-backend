package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.code-enum")
public record CodeEnumProperties(

        String tableName
) {
}

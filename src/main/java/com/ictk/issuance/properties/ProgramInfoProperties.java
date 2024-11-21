package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.program-info")
public record ProgramInfoProperties(
        String tableName
) {
}

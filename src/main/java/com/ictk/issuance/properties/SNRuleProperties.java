package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.sn-rule")
public record SNRuleProperties(
        String tableName
) {
}

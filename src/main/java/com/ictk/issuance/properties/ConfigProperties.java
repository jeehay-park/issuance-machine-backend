package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.config")
public record ConfigProperties(
        SubConfigProperties profile,
        SubConfigProperties keyissue,
        SubConfigProperties script
) {

    public record SubConfigProperties(
            String tableName
    ) {
    }

}

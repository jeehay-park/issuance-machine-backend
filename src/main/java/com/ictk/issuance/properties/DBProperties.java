package com.ictk.issuance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "issuance.db")
public record DBProperties (
        String driver,
        String ip,
        int port,
        String database,
        String user,
        String pass
) {
}

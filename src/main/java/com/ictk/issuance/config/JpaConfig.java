package com.ictk.issuance.config;

import com.ictk.issuance.properties.DBProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaConfig {

    private final DBProperties dbProperties;

    @Bean
    public DataSource dataSource()
    {
        log.info("DB dataSource ----------------- {} {} {} {} {}",
                dbProperties.ip(), dbProperties.port(), dbProperties.database(), dbProperties.user(), dbProperties.pass());
        String dbUrl = "jdbc:mariadb://"+dbProperties.ip()+":"+dbProperties.port()+"/"+dbProperties.database()
                +"?serverTimezone=asia/seoul&characterEncoding=UTF-8";
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(dbProperties.driver());
        dataSourceBuilder.url(dbUrl);
        dataSourceBuilder.username(dbProperties.user());
        dataSourceBuilder.password( dbProperties.pass() );
        return dataSourceBuilder.build();
    }

}

package com.ibosng.dbservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement

public class LhrDataSourceConfig {

    @Getter
    @Value("${database.lhrDbUrl:#{null}}")
    private String jdbcUrl;

    @Getter
    @Value("${database.lhrDbUsername:#{null}}")
    private String jdbcUsername;

    @Getter
    @Value("${database.lhrDbPassword:#{null}}")
    private String jdbcPasswort;

    @Bean(name = "lhrDataSource")
    public DataSource lhrDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.url(getJdbcUrl());
        dataSourceBuilder.username(getJdbcUsername());
        dataSourceBuilder.password(getJdbcPasswort());

        DataSource dataSource = dataSourceBuilder.build();

        return dataSource;
    }

}

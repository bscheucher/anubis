package com.ibosng._config;

import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = {"com.ibosng.dbservice.repositories", "com.ibosng.personalverwaltung.persistence", "com.ibosng.teilnehmerportal.repository"},
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager")
@ComponentScan(basePackages = {"com.ibosng.dbservice.services", "com.ibosng.dbservice.repositories", "com.ibosng.personalverwaltung.persistence", "com.ibosng.teilnehmerportal"})
@EntityScan({"com.ibosng.dbservice.entities", "com.ibosng.personalverwaltung.persistence", "com.ibosng.teilnehmerportal.entity"})
@EnableTransactionManagement
@Profile("!test")
public class DataSourceConfig {

    @Getter
    @Value("${database.ibosngDbUrl:#{null}}")
    private String jdbcUrl;

    @Getter
    @Value("${database.ibosngDbUsername:#{null}}")
    private String jdbcUsername;

    @Getter
    @Value("${database.ibosngDbPassword:#{null}}")
    private String jdbcPassword;

    @Bean
    @Primary
    public DataSource postgresDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(getJdbcUrl());
        dataSourceBuilder.username(getJdbcUsername());
        dataSourceBuilder.password(getJdbcPassword());

        DataSource dataSource = dataSourceBuilder.build();

        // Ensure timezone is set per session
        return new org.springframework.jdbc.datasource.DelegatingDataSource(dataSource) {
            @Override
            public Connection getConnection() throws SQLException {
                Connection connection = super.getConnection();
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("SET TIMEZONE TO 'Europe/Vienna'");
                }
                return connection;
            }
        };
    }

    @Bean(name = "postgresEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(postgresDataSource());
        em.setPackagesToScan("com.ibosng.dbservice.entities", "com.ibosng.personalverwaltung.persistence", "com.ibosng.teilnehmerportal.entity" );
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean(name = "postgresTransactionManager")
    @Primary
    public JpaTransactionManager postgresTransactionManager(final @org.springframework.beans.factory.annotation.Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "validate");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "false");
        hibernateProperties.setProperty("defer-datasource-initialization", "true");

        return hibernateProperties;
    }
}

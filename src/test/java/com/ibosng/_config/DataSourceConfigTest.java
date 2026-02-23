package com.ibosng._config;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = {"com.ibosng.dbservice.repositories", "com.ibosng.personalverwaltung.persistence"},
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager")
@EntityScan(basePackages = {"com.ibosng.dbservice.entities", "com.ibosng.personalverwaltung.persistence"})
@EnableTransactionManagement
@Slf4j
@Profile("test")
public class DataSourceConfigTest {

    @Value("${testcontainers.postgresql.image}")
    private String image;

    @Value("${testcontainers.postgresql.database}")
    private String databaseName;

    @Value("${testcontainers.postgresql.username}")
    private String username;

    @Value("${testcontainers.postgresql.password}")
    private String password;

    @Value("${testcontainers.postgresql.applyDbDump}")
    private Boolean applyDbDump;

    @Value("${testcontainers.postgresql.dbDumpPath}")
    private String dbDumpPath;


    @Bean
    public PostgreSQLContainer<?> postgresqlContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>(image)
                .withDatabaseName(databaseName)
                .withUsername(username)
                .withPassword(password)
                .withCopyFileToContainer(MountableFile.forHostPath("misc/postgres/init"), "/docker-entrypoint-initdb.d");
        container.start();

        if (applyDbDump) {
            var bashCommand = "gunzip -c /tmp/dump.sql.gz | psql -U %s -d %s > /dev/null 2>&1".formatted(username, databaseName);
            DbDumpApplier.apply(container, dbDumpPath, bashCommand);
        } else {
            log.info("PostgreSQL: Skip applying db dump");
        }
        return container;
    }

    @Primary
    @Bean
    @DependsOn("postgresqlContainer")
    public DataSource postgresDataSource(PostgreSQLContainer<?> postgreSQLContainer) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(postgreSQLContainer.getJdbcUrl());
        dataSource.setUsername(postgreSQLContainer.getUsername());
        dataSource.setPassword(postgreSQLContainer.getPassword());
        return dataSource;
    }

    @Bean(name = "postgresEntityManagerFactory")
    @Primary
    @DependsOn("postgresDataSource")
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(PostgreSQLContainer postgreSQLContainer) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(postgresDataSource(postgreSQLContainer));
        em.setPackagesToScan("com.ibosng.dbservice.entities","com.ibosng.personalverwaltung.persistence");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean(name = "postgresTransactionManager")
    public JpaTransactionManager postgresTransactionManager(@Qualifier("postgresEntityManagerFactory") final EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "validate");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");

        return hibernateProperties;
    }
}

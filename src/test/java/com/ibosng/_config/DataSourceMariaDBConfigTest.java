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
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.MountableFile;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = {"com.ibosng.dbibosservice.repositories"},
        entityManagerFactoryRef = "mariaDbEntityManagerFactory",
        transactionManagerRef = "mariaDbTransactionManager")
@ComponentScan(basePackages = {"com.ibosng.dbibosservice.services"})
@EntityScan("com.ibosng.dbibosservice.entities")
@EnableTransactionManagement
@Slf4j
@Profile({"test"})
public class DataSourceMariaDBConfigTest {

    @Value("${testcontainers.mariadb.image}")
    private String image;

    @Value("${testcontainers.mariadb.database}")
    private String databaseName;

    @Value("${testcontainers.mariadb.username}")
    private String username;

    @Value("${testcontainers.mariadb.password}")
    private String password;

    @Value("${testcontainers.mariadb.applyDbDump}")
    private Boolean applyDbDump;

    @Value("${testcontainers.mariadb.dbDumpPath}")
    private String dbDumpPath;


    @Bean
    public MariaDBContainer<?> mariaDbContainer() {
        MariaDBContainer<?> container = new MariaDBContainer<>(image)
                .withDatabaseName(databaseName)
                .withUsername(username)
                .withPassword(password)
                .withCopyFileToContainer(MountableFile.forHostPath("misc/mariadb/init"), "/docker-entrypoint-initdb.d");

        container.start();

        if (applyDbDump) {
            var bashCommand = "gunzip -c /tmp/dump.sql.gz | mariadb -u %s -p%s %s".formatted(username, password, databaseName);
            DbDumpApplier.apply(container, dbDumpPath, bashCommand);
        } else {
            log.info("MariaDB: Skip applying db dump");
        }
        return container;
    }

    @Bean(name = "mariaDbDataSource")
    @DependsOn("mariaDbContainer")
    public DataSource mariaDbDataSource(MariaDBContainer<?> mariaDBContainer) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl(mariaDBContainer.getJdbcUrl());
        dataSource.setUsername(mariaDBContainer.getUsername());
        dataSource.setPassword(mariaDBContainer.getPassword());

        return dataSource;
    }

    @Bean(name = "mariaDbEntityManagerFactory")
    @DependsOn("mariaDbDataSource")
    public LocalContainerEntityManagerFactoryBean mariaDbEntityManagerFactory(MariaDBContainer mariaDBContainer) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mariaDbDataSource(mariaDBContainer));
        em.setPackagesToScan("com.ibosng.dbibosservice.entities");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean(name = "mariaDbTransactionManager")
    public JpaTransactionManager mariaDbTransactionManager(@Qualifier("mariaDbEntityManagerFactory") final EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "validate");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");

        return hibernateProperties;
    }
}

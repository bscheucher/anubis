package com.ibosng.dbibosservice.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = {"com.ibosng.dbibosservice.repositories"},
        entityManagerFactoryRef = "mariaDbEntityManagerFactory",
        transactionManagerRef = "mariaDbTransactionManager")
@ComponentScan(basePackages = {"com.ibosng.dbibosservice.services"})
@EntityScan("com.ibosng.dbibosservice.entities")
@EnableTransactionManagement
@Profile("!test")
public class DataSourceMariaDBConfig {

    @Value("${mariaDBUrl:#{null}}")
    private String mariaDbUrl;

    @Value("${mariaDBUsername:#{null}}")
    private String mariaDbUsername;

    @Value("${mariaDBPassword:#{null}}")
    private String mariaDbPassword;

    @Bean(name = "mariaDbDataSource")
    public HikariDataSource mariaDbDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setJdbcUrl(mariaDbUrl);
        dataSource.setUsername(mariaDbUsername);
        dataSource.setPassword(mariaDbPassword);
        // HikariCP specific settings
        dataSource.setMaxLifetime(30000);
        dataSource.setConnectionTimeout(30000);
        dataSource.setValidationTimeout(10000);
        dataSource.setMaximumPoolSize(10);
        return dataSource;
    }


    @Bean(name = "mariaDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mariaDbEntityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mariaDbDataSource());
        em.setPackagesToScan("com.ibosng.dbibosservice.entities");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean(name = "mariaDbTransactionManager")
    public JpaTransactionManager mariaDbTransactionManager(final @org.springframework.beans.factory.annotation.Qualifier("mariaDbEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "validate");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "false");
        hibernateProperties.setProperty("defer-datasource-initialization", "true");
        return hibernateProperties;
    }
}

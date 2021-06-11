package com.hlushkov.movieland.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
public class TestConfiguration {
    @Value("${jdbc.maximum.pool.size}")
    private int maximumPoolSize;

    private PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:13.1");

    @Bean
    public DataSource dataSource() {
        log.info("Postgres container database name: {}", postgresContainer.getDatabaseName());
        log.info("Postgres container username: {}", postgresContainer.getUsername());
        log.info("Postgres container password: {}", postgresContainer.getPassword());
        postgresContainer.start();
        log.info("Postgres container jdbc url: {}", postgresContainer.getJdbcUrl());

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(postgresContainer.getJdbcUrl());
        hikariConfig.setUsername(postgresContainer.getUsername());
        hikariConfig.setPassword(postgresContainer.getPassword());
        hikariConfig.setDriverClassName(postgresContainer.getDriverClassName());
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        Flyway flyway = Flyway.configure().dataSource(hikariDataSource)
                .locations("classpath:db/migration/initial").baselineOnMigrate(true).load();

        flyway.migrate();
        return hikariDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() {

        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.hlushkov.movieland.entity");
        factoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        factoryBean.setPersistenceUnitName("dbRiderEntityFactoryBean");

        final Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", postgresContainer.getDriverClassName());
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.connection.username", postgresContainer.getUsername());
        properties.setProperty("hibernate.connection.password", postgresContainer.getPassword());
        properties.setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl());
        factoryBean.setJpaProperties(properties);
        log.info("ENTITY MANAGER FACTORY BEAN CREATED BY TestConfiguration!");
        return factoryBean;
    }

/*    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }*/

}

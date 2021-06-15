package com.hlushkov.movieland.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
public class TestConfiguration {
    @Value("${hibernate.connection.pool_size}")
    private int maximumPoolSize;

    @Bean
    public PostgreSQLContainer postgreSQLContainer() {
        PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:13.1");
        postgreSQLContainer.start();
        return postgreSQLContainer;
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer postgresContainer) {
        log.info("Postgres container database name: {}", postgresContainer.getDatabaseName());
        log.info("Postgres container username: {}", postgresContainer.getUsername());
        log.info("Postgres container password: {}", postgresContainer.getPassword());
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
    public SessionFactory sessionFactory(PostgreSQLContainer postgresContainer) {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", postgresContainer.getDriverClassName());
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.connection.username", postgresContainer.getUsername());
        properties.setProperty("hibernate.connection.password", postgresContainer.getPassword());
        properties.setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl());

        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.addAnnotatedClass(com.hlushkov.movieland.entity.Movie.class);
        configuration.addAnnotatedClass(com.hlushkov.movieland.entity.Genre.class);
        configuration.setProperties(properties);

        return configuration.buildSessionFactory();
    }

}

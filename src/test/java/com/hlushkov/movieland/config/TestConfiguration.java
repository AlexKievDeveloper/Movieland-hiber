package com.hlushkov.movieland.config;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.util.EntityManagerProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class TestConfiguration {
    @Bean
    public LocalEntityManagerFactoryBean entityManagerFactory() {
        LocalEntityManagerFactoryBean localEmfBean =
                new LocalEntityManagerFactoryBean();
        localEmfBean.setPersistenceUnitName("riderDB");
        return localEmfBean;
    }

    @Rule
    public EntityManagerProvider emProvider = EntityManagerProvider.instance("riderDB");
    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(emProvider.connection());

    @Bean
    public DataSource dataSource(@Value("${jdbc.maximum.pool.size}") int maximumPoolSize) {

        PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:13.1");

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

}



package com.hlushkov.movieland.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.listener.ChainListener;
import net.ttddyy.dsproxy.listener.DataSourceQueryCountListener;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
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

    @Bean("originalDataSource")
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
    @Primary
    public DataSource dataSource(@Qualifier("originalDataSource") DataSource dataSource) {
        ChainListener listener = new ChainListener();
        SLF4JQueryLoggingListener loggingListener = new SLF4JQueryLoggingListener();
        listener.addListener(loggingListener);
        listener.addListener(new DataSourceQueryCountListener());
        return ProxyDataSourceBuilder
                .create(dataSource)
                .name("DS-Proxy")
                .listener(listener)
                .build();
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.hlushkov.movieland");
        sessionFactory.setHibernateProperties(configureHibernateProperties());
        return sessionFactory;
    }

    @Bean
    protected DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    private Properties configureHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.generate_statistics", "true");
        properties.setProperty("hibernate.cache.use_structured_entries", "true");

        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        properties.setProperty("hibernate.cache.use_query_cache", "true");
        properties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.JCacheRegionFactory");
        properties.setProperty("hibernate.javax.cache.missing_cache_strategy", "create");

        properties.setProperty("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");
        properties.setProperty("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");
        properties.setProperty("hibernate.javax.cache.uri", "jcache.xml");
        return properties;
    }
}

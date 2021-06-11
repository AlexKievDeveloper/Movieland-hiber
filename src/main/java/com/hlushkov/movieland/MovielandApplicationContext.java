package com.hlushkov.movieland;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(value = "com.hlushkov.movieland", excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "com.hlushkov.movieland.web"))
public class MovielandApplicationContext {
    @Value("${hibernate.connection.url}")
    private String url;
    @Value("${hibernate.connection.username}")
    private String userName;
    @Value("${hibernate.connection.password}")
    private String password;
    @Value("${hibernate.connection.driver_class}")
    private String driverClassName;
    @Value("${hibernate.connection.pool_size}")
    private int maximumPoolSize;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(userName);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() {

        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.hlushkov.movieland.entity");
        factoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        factoryBean.setPersistenceUnitName("dbRiderEntityFactoryBean");

        final Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", driverClassName);
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.connection.username", userName);
        properties.setProperty("hibernate.connection.password", password);
        properties.setProperty("hibernate.connection.url", url);
        properties.setProperty("hibernate.connection.pool_size", String.valueOf(maximumPoolSize));
        factoryBean.setJpaProperties(properties);
        log.info("ENTITY MANAGER FACTORY BEAN CREATED BY TestConfiguration!");
        return factoryBean;
    }

/*    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }*/

}

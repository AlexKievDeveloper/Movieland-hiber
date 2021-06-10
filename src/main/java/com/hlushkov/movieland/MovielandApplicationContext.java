package com.hlushkov.movieland;

import com.hlushkov.movieland.dao.impl.HibernateMovieDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Slf4j
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(value = "com.hlushkov.movieland", excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "com.hlushkov.movieland.web"))
public class MovielandApplicationContext {

/*    @Bean
    public DataSource dataSource(@Value("${jdbc.url}") String url,
                                 @Value("${jdbc.user}") String userName,
                                 @Value("${jdbc.password}") String password,
                                 @Value("${jdbc.driver}") String driverClassName,
                                 @Value("${jdbc.maximum.pool.size}") int maximumPoolSize) {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(userName);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        return new HikariDataSource(hikariConfig);
    }*/

    /*    @Bean
        public SessionFactory sessionFactory() {
            return new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
        }
    */
    @Bean
    public LocalEntityManagerFactoryBean entityManagerFactory() {
        LocalEntityManagerFactoryBean localEmfBean =
                new LocalEntityManagerFactoryBean();
        localEmfBean.setPersistenceUnitName("mainHibernateUnit");
        return localEmfBean;
    }

    @Bean
    public HibernateMovieDao hibernateMovieDao() {
        log.info("HIBERNATE MOVIE DAO BEAN CREATED");
        return new HibernateMovieDao();
    }


}

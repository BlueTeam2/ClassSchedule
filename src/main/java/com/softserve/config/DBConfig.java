package com.softserve.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Objects;
import java.util.Properties;

import static org.hibernate.cfg.Environment.*;

@Configuration
@PropertySource("classpath:hibernate.properties")
@EnableTransactionManagement
@Slf4j
public class DBConfig {
    private static final String ENTITY_PACKAGE = "hibernate.entity.package";

    private final Environment environment;

    @Autowired
    public DBConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource getDataSource() {
        String url;
        String user;
        String password;
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(Objects.requireNonNull(environment.getProperty(DRIVER)));
        } catch (PropertyVetoException e) {
            System.exit(1);
        }

        url = System.getenv("HEROKU_DB_URL");
        user = System.getenv("HEROKU_DB_USER");
        password = System.getenv("HEROKU_DB_PASSWORD");

        if (url == null) {
            url = Objects.requireNonNull(environment.getProperty(URL));
        }
        if (user == null) {
            user = Objects.requireNonNull(environment.getProperty(USER));
        }
        if (password == null) {
            password = Objects.requireNonNull(environment.getProperty(PASS));
        }

        dataSource.setJdbcUrl(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        return dataSource;
    }


    @Bean
    @DependsOn("liquibase")
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(getDataSource());
        Properties properties = new Properties();

        String url = System.getenv("HEROKU_DB_URL");
        String user = System.getenv("HEROKU_DB_USER");
        String password = System.getenv("HEROKU_DB_PASSWORD");
        if (url != null && user != null && password != null) {
            properties.put("hibernate.connection.url", url);
            properties.put("hibernate.connection.username", user);
            properties.put("hibernate.connection.password", password);
        }
        properties.put(SHOW_SQL, Objects.requireNonNull(environment.getProperty(SHOW_SQL)));
        properties.put(HBM2DDL_AUTO, Objects.requireNonNull(environment.getProperty(HBM2DDL_AUTO)));
        properties.put(DIALECT, Objects.requireNonNull(environment.getProperty(DIALECT)));

        properties.put(C3P0_MIN_SIZE, Objects.requireNonNull(environment.getProperty(C3P0_MIN_SIZE)));
        properties.put(C3P0_MAX_SIZE, Objects.requireNonNull(environment.getProperty(C3P0_MAX_SIZE)));
        properties.put(C3P0_ACQUIRE_INCREMENT, Objects.requireNonNull(environment.getProperty(C3P0_ACQUIRE_INCREMENT)));
        properties.put(C3P0_TIMEOUT, Objects.requireNonNull(environment.getProperty(C3P0_TIMEOUT)));
        properties.put(C3P0_MAX_STATEMENTS, Objects.requireNonNull(environment.getProperty(C3P0_MAX_STATEMENTS)));

        sessionFactoryBean.setHibernateProperties(properties);
        sessionFactoryBean.setPackagesToScan(environment.getProperty(ENTITY_PACKAGE));

        return sessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());
        return transactionManager;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.yaml");
        liquibase.setDataSource(getDataSource());
        liquibase.setShouldRun(environment.getProperty("liquibase.should_run", Boolean.class, Boolean.TRUE));
        return liquibase;
    }

}

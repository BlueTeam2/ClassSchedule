package com.softserve.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@PropertySource("classpath:hibernatetest.properties")
@EnableTransactionManagement
public class DBConfigTest {
    private static final String ENTITY_PACKAGE = "hibernate.entity.package";

    private final Environment environment;

    @Autowired
    public DBConfigTest(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource getDataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(Objects.requireNonNull(environment.getProperty(DRIVER)));
        } catch (PropertyVetoException e) {
            System.exit(1);
        }

        dataSource.setJdbcUrl(Objects.requireNonNull(environment.getProperty(URL)));
        dataSource.setUser(Objects.requireNonNull(environment.getProperty(USER)));
        dataSource.setPassword(Objects.requireNonNull(environment.getProperty(PASS)));
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(getDataSource());
        Properties properties = new Properties();

        properties.put("hibernate.connection.url", environment.getProperty(URL));
        properties.put("hibernate.connection.username", environment.getProperty(USER));
        properties.put("hibernate.connection.password", environment.getProperty(PASS));

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

}

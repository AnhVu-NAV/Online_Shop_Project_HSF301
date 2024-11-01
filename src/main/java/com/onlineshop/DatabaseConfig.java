package com.onlineshop;

import com.onlineshop.common.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

import static jakarta.persistence.PersistenceConfiguration.JDBC_URL;

@Configuration
@PropertySource("classpath:database.properties")
@EnableTransactionManagement
@ComponentScan
public class DatabaseConfig implements Constants.DatabaseConfig {
    private final Environment environment; //Use org.springframework.core.env.Environment class

    public DatabaseConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean(value = "dataSource", name = {"dataSource"}, autowireCandidate = true)
    DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty(JDBC_DRIVER_CLASSNAME,
                JDBC_DRIVER_CLASSNAME_DEFAULT));
        dataSource.setUrl(environment.getProperty(JDBC_URL));
        dataSource.setUsername(environment.getProperty(JDBC_USERNAME));
        dataSource.setPassword(environment.getProperty(JDBC_PASSWORD));
        return dataSource;
    }
//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//
//    }
    @Bean(autowireCandidate = true)
    HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        System.out.println("--->getTransactionManager() = " + transactionManager ==  null ? " null " : "getTransactionManager is Not null");
        return transactionManager;
    }

    @Bean(autowireCandidate = true)
    LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.onlineshop.entity",
                "com.onlineshop", "com.onlineshop.dao",
                "com.onlineshop.dao.impl",  "com.onlineshop.service.impl" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        System.out.println("--> ...sessionFactory= " + sessionFactory ==  null ? " null " : "SessionFactory is Not null");
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(HIBERNATE_DIALECT, environment.getProperty(HIBERNATE_DIALECT));
        properties.put(HIBERNATE_SHOW_SQL, environment.getProperty(HIBERNATE_SHOW_SQL));
        properties.put(HIBERNATE_FORMAT_SQL, environment.getProperty(HIBERNATE_FORMAT_SQL));
        properties.put(HIBERNATE_HBM2DDL_AUTO, environment.getProperty(HIBERNATE_HBM2DDL_AUTO));
        return properties;
    }
}

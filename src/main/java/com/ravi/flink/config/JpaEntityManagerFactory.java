package com.ravi.flink.config;

import com.ravi.flink.entity.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceUnitInfo;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.postgresql.ds.PGSimpleDataSource;


import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class JpaEntityManagerFactory {

    private final DataSourceProperties dataSourceProperties;

    public JpaEntityManagerFactory(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        PersistenceUnitInfo persistenceUnitInfo = getPersistenceUnitInfo(getClass().getSimpleName());
        return new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(persistenceUnitInfo), Map.of())
                .build();
    }

    protected HibernatePersistenceUnitInfo getPersistenceUnitInfo(String name) {
        DataSource dataSource = getHikariDataSource();
        return new HibernatePersistenceUnitInfo(name, getEntityClassNames(), getProperties(dataSource));
    }

    protected List<String> getEntityClassNames() {
        Class[] entityClasses = new Class[]{
                User.class
        };
        return Arrays.stream(entityClasses)
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    protected Properties getProperties(DataSource dataSource) {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dataSourceProperties.getDialect());
        properties.put("hibernate.id.new_generator_mappings", false);
        properties.put("hibernate.connection.datasource", dataSource);
        return properties;
    }

    protected DataSource getPGSimpleDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(dataSourceProperties.getJdbcUrl());
        dataSource.setUser(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());

        return dataSource;
    }

    protected DataSource getHikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dataSourceProperties.getJdbcUrl());
        hikariConfig.setUsername(dataSourceProperties.getUsername());
        hikariConfig.setPassword(dataSourceProperties.getPassword());
        hikariConfig.setIdleTimeout(dataSourceProperties.getIdleTimeout());
        hikariConfig.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
        hikariConfig.setMinimumIdle(dataSourceProperties.getMinimumIdle());
        hikariConfig.setConnectionTimeout(dataSourceProperties.getConnectionTimeout());
        hikariConfig.setDriverClassName(dataSourceProperties.getDriverClassName());

        return new HikariDataSource(hikariConfig);
    }
}

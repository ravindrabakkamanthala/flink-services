package com.ravi.flink.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.ravi.flink.config.DataSourceProperties;
import com.ravi.flink.config.JpaEntityManagerFactory;
import jakarta.persistence.EntityManagerFactory;

public class DataSourceGuiceModule extends AbstractModule {

    private final DataSourceProperties dataSourceProperties;
    private EntityManagerFactory entityManagerFactory;

    public DataSourceGuiceModule(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Override
    protected void configure() {
        JpaEntityManagerFactory jpaEntityManagerFactory = new JpaEntityManagerFactory(this.dataSourceProperties);
        this.entityManagerFactory = jpaEntityManagerFactory.getEntityManagerFactory();
    }

    @Singleton
    @Provides
    public EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }
}

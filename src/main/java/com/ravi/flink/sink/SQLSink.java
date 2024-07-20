package com.ravi.flink.sink;

import com.google.inject.Guice;
import com.ravi.flink.config.DataSourceProperties;
import com.ravi.flink.entity.User;
import com.ravi.flink.guice.DataSourceGuiceModule;
import com.ravi.flink.repository.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.Objects;

public class SQLSink extends RichSinkFunction<User> {

    private final DataSourceProperties dataSourceProperties;
    private transient EntityManagerFactory entityManagerFactory;
    private transient UserRepository userRepository;

    public SQLSink(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        if (Objects.isNull(entityManagerFactory)) {
            var guice = Guice.createInjector(new DataSourceGuiceModule(this.dataSourceProperties));
            this.entityManagerFactory = guice.getInstance(EntityManagerFactory.class);
        }
        this.userRepository = new UserRepository(entityManagerFactory);
    }

    @Override
    public void invoke(User userObj, Context context) throws Exception {
        this.userRepository.save(userObj);
    }

    @Override
    public void close() throws Exception {
        if (Objects.nonNull(entityManagerFactory)) {
            entityManagerFactory.close();
        }

        super.close();
    }
}

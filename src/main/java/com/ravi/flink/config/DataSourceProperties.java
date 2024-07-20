package com.ravi.flink.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "datasource")
public class DataSourceProperties implements Serializable {

    private String jdbcUrl;
    private String driverClassName;
    private String username;
    private String password;
    private long idleTimeout;
    private long connectionTimeout;
    private int minimumIdle;
    private long maximumIdle;
    private int maximumPoolSize;
    private String dialect;

}

package com.ravi.flink.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.Properties;

@Data
@ConfigurationProperties(prefix = "kafka.consumer")
public class KafkaConsumerProperties implements Serializable {

    private Properties props;

}

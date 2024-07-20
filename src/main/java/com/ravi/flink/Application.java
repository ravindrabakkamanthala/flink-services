package com.ravi.flink;

import com.ravi.flink.config.KafkaConsumerProperties;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@SpringBootApplication
@ConfigurationPropertiesScan("com.ravi.flink")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public StreamExecutionEnvironment streamExecutionEnvironment() {
        return StreamExecutionEnvironment.getExecutionEnvironment();
    }

}

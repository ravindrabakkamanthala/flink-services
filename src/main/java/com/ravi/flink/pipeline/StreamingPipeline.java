package com.ravi.flink.pipeline;

import com.ravi.flink.config.DataSourceProperties;
import com.ravi.flink.config.KafkaConsumerProperties;
import com.ravi.flink.functions.DataProcessFunction;
import com.ravi.flink.model.UserProfileEvent;
import com.ravi.flink.serializer.UserProfileDeserializer;
import com.ravi.flink.sink.SQLSink;
import lombok.RequiredArgsConstructor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StreamingPipeline implements CommandLineRunner {

    private final StreamExecutionEnvironment environment;
    private final KafkaConsumerProperties consumerProperties;
    private final DataSourceProperties dataSourceProperties;

    public void run(String... args) throws Exception {
        buildPipeline();
        environment.execute("Streaming Job");
    }

    private void buildPipeline() {
        DataProcessFunction processFunction = new DataProcessFunction();
        SQLSink sinkFunction = new SQLSink(dataSourceProperties);

        @Deprecated
        FlinkKafkaConsumer<UserProfileEvent> kafkaConsumer = new FlinkKafkaConsumer<>(
                "USER_PROFILE",
                new UserProfileDeserializer(),
                consumerProperties.getProps()
        );

        @Deprecated
        DataStreamSource<UserProfileEvent> source = environment
                .addSource(kafkaConsumer, "kafka-source")
                .setParallelism(1);

        var processedData = source
                .name("Mapped Source")
                .process(processFunction)
                .setParallelism(2)
                .rescale();

        processedData
                .addSink(sinkFunction)
                .name("Sink to SQL")
                .setParallelism(2);
    }

}

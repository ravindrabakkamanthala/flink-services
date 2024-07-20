package com.ravi.flink;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ravi.flink.model.UserProfileEvent;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class KafkaProducerTest {
    private static Map<String, String> CONFIG;
    KafkaProducer<Object, GenericRecord> producer;

    public static void main(String[] args) throws JSONException, IOException {
        KafkaProducerTest kafkaProducer = new KafkaProducerTest(Map.of("kafka_url", "localhost:9092"));
        kafkaProducer.publish();
        kafkaProducer.close();
    }

    public KafkaProducerTest(Map<String, String> data) {
        KafkaProducerTest.CONFIG = data;
        producer = createKafkaProducer();
    }

    public void close() {
        producer.close();
    }

    public void publish() throws JSONException, IOException {
        JSONArray events = readEventPayload();
        publishToProductCache(events);
    }

    public void publishToProductCache(JSONArray events) throws JSONException, IOException {
        Schema schema = new Schema.Parser().parse(new File("src/main/resources/user.avsc"));
        String topicName = "USER_PROFILE";
        for (int i = 0; i < events.length(); i++) {
            JSONObject json = events.getJSONObject(i);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

            UserProfileEvent userProfileEvent = mapper.readValue(json.toString(), UserProfileEvent.class);

            GenericRecord genericRecord = new GenericData.Record(schema);
            genericRecord.put("firstName", userProfileEvent.getFirstName());
            genericRecord.put("lastName", userProfileEvent.getLastName());
            genericRecord.put("city", userProfileEvent.getLastName());
            genericRecord.put("country", userProfileEvent.getCountry());
            genericRecord.put("zipCode", userProfileEvent.getZipCode());

            String key = UUID.randomUUID().toString();
            ProducerRecord<Object, GenericRecord> record =
                    new ProducerRecord<>(topicName, key, genericRecord);
            producer.send(record);
        }
    }


    private JSONArray readEventPayload() {
        File file = FileUtils.getFile("src", "main", "resources", "user_profiles.json");
        try {
            return new JSONArray(FileUtils.readFileToString(file, "UTF-8"));
        } catch (IOException | JSONException e) {
            System.out.println("Failed to read event payload: " + e.getMessage());
        }
        return new JSONArray();
    }

    private static KafkaProducer<Object, GenericRecord> createKafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CONFIG.get("kafka_url"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());

        return new KafkaProducer<>(props);
    }
}

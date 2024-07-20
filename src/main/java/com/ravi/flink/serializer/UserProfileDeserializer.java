package com.ravi.flink.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ravi.flink.model.UserProfileEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

@Slf4j
public class UserProfileDeserializer implements DeserializationSchema<UserProfileEvent> {

    private transient ObjectMapper objectMapper;

    @Override
    public void open(InitializationContext context) throws Exception {
        DeserializationSchema.super.open(context);
        objectMapper = new ObjectMapper();
    }

    @Override
    public UserProfileEvent deserialize(byte[] bytes) throws IOException {
        try {
            return objectMapper.readValue(String.valueOf(bytes), UserProfileEvent.class);
        } catch (Exception e) {
            log.error("Exception Occurred while deserializing the message, %s", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isEndOfStream(UserProfileEvent userProfileEvent) {
        return false;
    }

    @Override
    public TypeInformation<UserProfileEvent> getProducedType() {
        return TypeInformation.of(UserProfileEvent.class);
    }

}

package com.ravi.flink.functions;

import com.ravi.flink.constants.Constants;
import com.ravi.flink.entity.User;
import com.ravi.flink.model.UserProfileEvent;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Objects;

public class DataProcessFunction extends ProcessFunction<UserProfileEvent, User> {

    public void processElement(
            UserProfileEvent event,
            ProcessFunction<UserProfileEvent, User>.Context context,
            Collector<User> collector
    ) {
        String fullName = String.format("%1$s, %2$s",
                Objects.nonNull(event.getFirstName()) ? event.getFirstName() : Constants.FNU,
                Objects.nonNull(event.getLastName()) ? event.getLastName() : Constants.LNU);

        String address = String.format("%1$s, %2$s - %3$x", event.getCity(), event.getCountry(), event.getZipCode());

        User userModel = User
                .builder()
                .fullName(fullName)
                .address(address)
                .build();

        collector.collect(userModel);
    }

}

package com.ravi.flink.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEvent implements Serializable {

    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private int zipCode;
}

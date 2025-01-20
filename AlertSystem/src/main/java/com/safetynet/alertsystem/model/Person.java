package com.safetynet.alertsystem.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private long zip;
    private String phone;
    private String email;

}

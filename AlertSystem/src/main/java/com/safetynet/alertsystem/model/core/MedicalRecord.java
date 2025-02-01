package com.safetynet.alertsystem.model.core;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

}

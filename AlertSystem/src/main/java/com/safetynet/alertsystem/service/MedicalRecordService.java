package com.safetynet.alertsystem.service;

import com.safetynet.alertsystem.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MedicalRecordService {

    private final Map<String, MedicalRecord> medicalRecords = new HashMap<>();

    // general
    private String generateMapKey(String firstName, String lastName) {
        return firstName + lastName;
    }
    public boolean checkIfRecordExists(String firstName, String lastName) {
        String key = generateMapKey(firstName, lastName);
        return medicalRecords.containsKey(key);
    }
    // create
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        String key = generateMapKey(medicalRecord.getFirstName(), medicalRecord.getLastName());
        medicalRecords.put(key, medicalRecord);
    }
    // read
    public MedicalRecord getMedicalRecord(String firstName, String lastName) {
        String key = generateMapKey(firstName, lastName);
        return medicalRecords.get(key);
    }
    public Map<String, MedicalRecord> getMedicalRecordsMap() {
        return medicalRecords;
    }
    // update
    public void updateMedicalRecord(MedicalRecord medicalRecord) {
        String key = generateMapKey(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (medicalRecords.containsKey(key)) {
            medicalRecords.put(key, medicalRecord);
        }
    }
    // delete
    public void deleteMedicalRecord(String firstName, String lastName) {
        String key = generateMapKey(firstName, lastName);
        medicalRecords.remove(key);
    }
}

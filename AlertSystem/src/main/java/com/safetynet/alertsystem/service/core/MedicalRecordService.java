package com.safetynet.alertsystem.service.core;

import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MedicalRecordService {

    private static final Logger logger = LogManager.getLogger(MedicalRecordService.class);
    private final Map<String, MedicalRecord> medicalRecords = new HashMap<>();
    private final JsonReaderRepository jsonReader;

    // data management
    @Autowired
    public MedicalRecordService(JsonReaderRepository jsonReader) {
        this.jsonReader = jsonReader;
        loadData();
    }
    private void loadData() {
        logger.debug("Entering loadData method");

        List<MedicalRecord> records = jsonReader.getData().getMedicalrecords();
        for (MedicalRecord record : records) {
            String key = generateMapKey(record.getFirstName(), record.getLastName());
            medicalRecords.put(key, record);
        }

        logger.info("Loaded {} medical records", medicalRecords.size());
        logger.debug("Exiting loadData method");
    }
    private void saveData() {
        jsonReader.getData().setMedicalrecords(new ArrayList<>(medicalRecords.values()));
        jsonReader.writeData();
        logger.info("Saved medical record");
    }
    // general
    private String generateMapKey(String firstName, String lastName) {
        logger.debug("Entering generateMapKey, for : {} {}", firstName, lastName);
        return firstName + lastName;
    }
    public boolean checkIfRecordExists(String firstName, String lastName) {
        logger.debug("Entering checkIfRecordExists, for : {} {}", firstName, lastName);
        String key = generateMapKey(firstName, lastName);

        boolean result = medicalRecords.containsKey(key);

        logger.info("exists ? {}", result);
        logger.debug("Exiting checkIfRecordExists");
        return result;
    }
    // create
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        logger.debug("Entering addMedicalRecord");

        String key = generateMapKey(medicalRecord.getFirstName(), medicalRecord.getLastName());
        medicalRecords.put(key, medicalRecord);
        saveData();

        logger.info("added medical record {}", key);
        logger.debug("Exiting addMedicalRecord");
    }
    // read
    public MedicalRecord getMedicalRecord(String firstName, String lastName) {
        logger.debug("Entering getMedicalRecord");

        String key = generateMapKey(firstName, lastName);
        MedicalRecord result = medicalRecords.get(key);

        logger.info("got {}'s medical record", key);
        logger.debug("Exiting getMedicalRecord");

        return result;
    }
    public Map<String, MedicalRecord> getMedicalRecordsMap() {
        logger.debug("Entering getMedicalRecordsMap");
        logger.info("records found {}", medicalRecords.size());

        return medicalRecords;
    }
    public List<MedicalRecord> getAllMedicalRecords() {
        logger.debug("Entering getAllMedicalRecords");

        List<MedicalRecord> records = jsonReader.getData().getMedicalrecords();

        logger.info("records found {}", records.size());
        logger.debug("Exiting getAllMedicalRecords");
        return records;
    }
    public MedicalRecord getMedicalRecordByName(String firstName, String lastName) {
        logger.debug("Entering getMedicalRecordByName, for : {} {}", firstName, lastName);
        String key = generateMapKey(firstName, lastName);

        MedicalRecord result = medicalRecords.get(key);

        logger.info("got {}'s medical record", key);
        logger.debug("Exiting getMedicalRecordByName, for : {} {}", key, result);
        return result;
    }
    // update
    public void updateMedicalRecord(MedicalRecord medicalRecord) {
        logger.debug("Entering updateMedicalRecord");

        String key = generateMapKey(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (medicalRecords.containsKey(key)) {
            medicalRecords.put(key, medicalRecord);
            saveData();
            logger.info("updated {}'s record", key);
        } else {
            logger.warn("no medical record found for {}", key);
        }
        logger.debug("exiting updateMedicalRecord");
    }
    // delete
    public void deleteMedicalRecord(String firstName, String lastName) {
        logger.debug("Entering deleteMedicalRecord");

        String key = generateMapKey(firstName, lastName);
        medicalRecords.remove(key);
        saveData();

        logger.info("deleted {}'s record", key);
        logger.debug("exiting deleteMedicalRecord");
    }
}

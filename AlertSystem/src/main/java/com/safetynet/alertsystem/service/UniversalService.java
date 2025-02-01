package com.safetynet.alertsystem.service;

import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.model.core.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UniversalService {

    private static final Logger logger = LogManager.getLogger(UniversalService.class);

    public int getAgeFromBirthdate(String birthdate) {
        logger.debug("Getting age of birthdate {}", birthdate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthdateDate = LocalDate.parse(birthdate, formatter);
        int age = Period.between(birthdateDate, LocalDate.now()).getYears();

        logger.info("getAgeFromBirthdate: " + birthdateDate + ", age: " + age);
        logger.debug("Exiting getAgeFromBirthdate");

        return age;
    }

    public List<MedicalRecord> getMedicalRecord(Person person, List<MedicalRecord> records) {
        logger.debug("Getting medical records for {}", person.getFirstName() + " " + person.getLastName());

        List<MedicalRecord> results =  records.stream()
                .filter(record ->
                        record.getFirstName().equals(person.getFirstName())
                        && record.getLastName().equals(person.getLastName()))
                .toList();

        logger.info("Found {} records", results.size());
        logger.debug("Exiting getMedicalRecord");

        return results;
    }

    // get a map of personal details and medical records
    public Map<String, Object> getPersonalDetails(Person person, List<MedicalRecord> medicalRecords, boolean phone, boolean email, boolean address) {
        logger.debug("Getting personal details for {}", person.getFirstName() + " " + person.getLastName());

        Map<String, Object> personDetails = new LinkedHashMap<>();
        personDetails.put("firstName", person.getFirstName());
        personDetails.put("lastName", person.getLastName());

        if (phone) {
            personDetails.put("phone", person.getPhone());
            logger.info("Phone number is {}", person.getPhone());
        }
        if (email) {
            personDetails.put("email", person.getEmail());
            logger.info("Email is {}", person.getEmail());
        }
        if (address) {
            personDetails.put("address", person.getAddress());
            logger.info("Address is {}", person.getAddress());
        }

        if (medicalRecords != null) {
            logger.info("medical records present");

            List<MedicalRecord> medicalRecordList = getMedicalRecord(person, medicalRecords);

            String birthdate = (medicalRecordList.isEmpty()) ? null : medicalRecordList.get(0).getBirthdate();

            int age = getAgeFromBirthdate(birthdate);

            List<String> medications = medicalRecordList.isEmpty() ?
                    Collections.emptyList() : medicalRecordList.get(0).getMedications();
            List<String> allergies = medicalRecordList.isEmpty() ?
                    Collections.emptyList() : medicalRecordList.get(0).getAllergies();

            personDetails.put("age", age);
            personDetails.put("medications", medications);
            personDetails.put("allergies", allergies);
            logger.info("is {} years old, medications {}, allergies {}", age, medications.size(), allergies.size());
        }

        logger.debug("Exiting getPersonalDetails");
        return personDetails;
    }

}

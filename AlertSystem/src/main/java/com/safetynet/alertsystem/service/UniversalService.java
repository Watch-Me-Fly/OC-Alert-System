package com.safetynet.alertsystem.service;

import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.model.core.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UniversalService {

    public int getAgeFromBirthdate(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthdateDate = LocalDate.parse(birthdate, formatter);
        return Period.between(birthdateDate, LocalDate.now()).getYears();
    }

    public List<MedicalRecord> getMedicalRecord(Person person, List<MedicalRecord> records) {

        return records.stream()
                .filter(record ->
                        record.getFirstName().equals(person.getFirstName())
                        && record.getLastName().equals(person.getLastName()))
                .toList();
    }

    // get a map of personal details and medical records
    public Map<String, Object> getPersonalDetails(Person person, List<MedicalRecord> medicalRecords, boolean phone, boolean email, boolean address) {

        Map<String, Object> personDetails = new HashMap<>();
        personDetails.put("firstName", person.getFirstName());
        personDetails.put("lastName", person.getLastName());

        if (medicalRecords != null) {
            List<MedicalRecord> medicalRecordList = getMedicalRecord(person, medicalRecords);

            String birthdate = (medicalRecordList.isEmpty()) ? null : medicalRecordList.get(0).getBirthdate();

            int age = getAgeFromBirthdate(birthdate);

            List<String> medications = medicalRecordList.isEmpty() ?
                    Collections.emptyList() : medicalRecordList.get(0).getMedications();
            List<String> allergies = medicalRecordList.isEmpty() ?
                    Collections.emptyList() : medicalRecordList.get(0).getAllergies();

            personDetails.put("medications", medications);
            personDetails.put("allergies", allergies);
            personDetails.put("age", age);
        }

        if (phone) {
            personDetails.put("phone", person.getPhone());
        }
        if (email) {
            personDetails.put("email", person.getEmail());
        }
        if (address) {
            personDetails.put("address", person.getAddress());
        }

        return personDetails;
    }

}

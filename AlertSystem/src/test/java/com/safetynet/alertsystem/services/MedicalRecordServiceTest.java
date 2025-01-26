package com.safetynet.alertsystem.services;

import com.safetynet.alertsystem.model.MedicalRecord;
import com.safetynet.alertsystem.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class MedicalRecordServiceTest {

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @BeforeAll
    static void setUp() {
    }

    // create
    @DisplayName("Add medical record, verify medical records map")
    @Test
    void addMedicalRecordTest() {
        // arrange
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2000", Arrays.asList("Bactrim"), Arrays.asList("Strawberries"));
        // act
        medicalRecordService.addMedicalRecord(medicalRecord);
        Map<String, MedicalRecord> recordsMap = medicalRecordService.getMedicalRecordsMap();
        // assert
        assertEquals(1, recordsMap.size());
    }
    // read
    @DisplayName("Get a medical record by first and last names")
    @Test
    void getMedicalRecordTest() {
        // arrange
        MedicalRecord record1 = new MedicalRecord("John", "Doe", "01/01/2000", Arrays.asList("Bactrim"), Arrays.asList("Strawberries"));
        MedicalRecord record2 = new MedicalRecord("Tina", "Turner", "01/06/1949", Arrays.asList(), Arrays.asList());
        medicalRecordService.addMedicalRecord(record1);
        medicalRecordService.addMedicalRecord(record2);
        // act
        MedicalRecord recToGet = medicalRecordService.getMedicalRecord("John", "Doe");
        // assert
        assertEquals("01/01/2000", recToGet.getBirthdate());
        }
    // update
    @DisplayName("Update an existing medical record")
    @Test
    void updateMedicalRecordTest() {
        // arrange
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2000", Arrays.asList("Bactrim"), Arrays.asList());
        medicalRecordService.addMedicalRecord(medicalRecord);
        medicalRecord.setAllergies(Arrays.asList("Strawberries"));
        // act
        medicalRecordService.updateMedicalRecord(medicalRecord);
        // assert
        assertEquals("Strawberries", medicalRecord.getAllergies().get(0));
        }
    @DisplayName("Try to update a non-existing medical record - ERR")
    @Test
    void updateNonExistingMedicalRecordTest() {
        // arrange
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2000", Arrays.asList("Bactrim"), Arrays.asList());
        medicalRecord.setAllergies(Arrays.asList("Strawberries"));
        // act
        medicalRecordService.updateMedicalRecord(medicalRecord);
        // assert
        assertEquals(0, medicalRecordService.getMedicalRecordsMap().size());
    }
    // delete
    @DisplayName("Delete a medical record")
    @Test
    void deleteMedicalRecordTest() {
        // arrange
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2000", Arrays.asList("Bactrim"), Arrays.asList());
        medicalRecordService.addMedicalRecord(medicalRecord);
        // act
        medicalRecordService.deleteMedicalRecord("John", "Doe");
        // assert
        assertFalse(medicalRecordService.checkIfRecordExists("John", "Doe"));
    }

}

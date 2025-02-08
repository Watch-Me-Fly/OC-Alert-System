package com.safetynet.alertsystem.services.core;

import com.safetynet.alertsystem.model.Data;
import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.core.MedicalRecordService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class MedicalRecordServiceTest {

    private static MedicalRecordService medicalRecordService;
    private static JsonReaderRepository jsonReader;
    private static List<MedicalRecord> recordsList;

    @BeforeAll
    static void setUp() {
        jsonReader = Mockito.mock(JsonReaderRepository.class);
    }
    @BeforeEach
    void init() {
        recordsList = Arrays.asList(
                new MedicalRecord("John", "Doe", "05/15/2010", Collections.emptyList(), Collections.emptyList()),
                new MedicalRecord("Jane", "Doe", "10/20/1985", Collections.emptyList(), Collections.emptyList())
        );
        Data data = new Data();
        data.setMedicalrecords(recordsList);
        Mockito.doReturn(data).when(jsonReader).getData();

        medicalRecordService = new MedicalRecordService(jsonReader);
    }

    // create
    @DisplayName("Add medical record, verify medical records map")
    @Test
    void addMedicalRecordTest() {
        // arrange
        MedicalRecord medicalRecord = new MedicalRecord("Clash", "Doe", "01/01/2000", Arrays.asList("Bactrim"), Arrays.asList("Strawberries"));
        // act
        medicalRecordService.addMedicalRecord(medicalRecord);
        Map<String, MedicalRecord> recordsMap = medicalRecordService.getMedicalRecordsMap();
        // assert
        assertEquals(3, recordsMap.size());
    }
    // read
    @DisplayName("Get a medical record by first and last names")
    @Test
    void getMedicalRecordTest() {
        // act
        MedicalRecord recToGet = medicalRecordService.getMedicalRecord("John", "Doe");
        // assert
        assertEquals("05/15/2010", recToGet.getBirthdate());
        }
    // update
    @DisplayName("Update an existing medical record")
    @Test
    void updateMedicalRecordTest() {
        // arrange
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2000", Arrays.asList("Bactrim"), Arrays.asList());
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
        MedicalRecord medicalRecord = new MedicalRecord("Tine", "Turner", "01/01/2000", Arrays.asList(), Arrays.asList());
        medicalRecord.setAllergies(Arrays.asList("Strawberries"));
        // act
        medicalRecordService.updateMedicalRecord(medicalRecord);
        // assert
        assertFalse(medicalRecordService.checkIfRecordExists("Tina", "Turner"));

    }
    // delete
    @DisplayName("Delete a medical record")
    @Test
    void deleteMedicalRecordTest() {
        // act
        medicalRecordService.deleteMedicalRecord("John", "Doe");
        // assert
        assertFalse(medicalRecordService.checkIfRecordExists("John", "Doe"));
    }

}

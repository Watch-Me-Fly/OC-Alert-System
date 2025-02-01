package com.safetynet.alertsystem.controllers.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alertsystem.controller.core.MedicalRecordsController;
import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.core.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordsControllerTest {

    @Mock
    private MedicalRecordsController controller;
    @Mock
    private MedicalRecordService service;
    @Mock
    private MedicalRecord record;
    @Autowired
    private JsonReaderRepository jsonReader;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new MedicalRecordsController(service, jsonReader);
        objectMapper = new ObjectMapper();
        record = new MedicalRecord("John",
                "Doe",
                "01/01/2000",
                Arrays.asList("Bactrim"),
                Arrays.asList("Strawberries"));
    }

    private void addMedicalRecord(MedicalRecord recordToAdd) throws Exception{
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordToAdd)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Medical record added"));
    }

    @DisplayName("/medicalRecord/ Engpoint = 200 [OK]")
    @Test
    void testRecordsListOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/medicalRecord/")).andExpect(MockMvcResultMatchers.status().isOk());
    }
    @DisplayName("Add a record - OK")
    @Test
    void testAddingRecord() throws Exception {
        addMedicalRecord(record);
    }
    @DisplayName("Update a record that exists")
    @Test
    void testUpdatingRecordOK() throws Exception {

        addMedicalRecord(record);

        MedicalRecord updatedRecord = new MedicalRecord("John",
                "Doe",
                "01/01/2000",
                Arrays.asList(),
                Arrays.asList());

        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRecord)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Medical record updated"));
    }
    @DisplayName("Update a record that does not exist")
    @Test
    void testUpdatingRecordNotFound() throws Exception {
        MedicalRecord updatedRecord = new MedicalRecord("Jane",
                "Doe",
                "01/01/1988",
                Arrays.asList(),
                Arrays.asList());

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecord)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Medical record not found"));
    }
    @DisplayName("Delete a medical record OK")
    @Test
    void testDeletingRecordOK() throws Exception {

        addMedicalRecord(record);

        mockMvc.perform(delete("/medicalRecord/John/Doe"))
                .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Medical record deleted"));
    }
    @DisplayName("Delete a non existing medical record")
    @Test
    void testDeletingRecordNotFound() throws Exception {

        mockMvc.perform(delete("/medicalRecord/John/Doe"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Medical record not found"));
    }
}

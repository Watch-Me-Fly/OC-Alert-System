package com.safetynet.alertsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alertsystem.controller.FireStationController;
import com.safetynet.alertsystem.model.FireStation;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.FireStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FireStationControllerTest {

    @Mock
    private FireStation station;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        station = new FireStation("123 sesame street", 4);
    }

    @DisplayName("/firestation/list Endpoint = 200 [OK]")
    @Test
    void testStationsListOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/firestation/list")).andExpect(MockMvcResultMatchers.status().isOk());
    }
    @DisplayName("Add a station - Ok")
    @Test
    void testAddStationOK() throws Exception {
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(station)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("FireStation created"));
    }
    @DisplayName("Update a station that exists")
    @Test
    void testUpdateStationOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(station)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        FireStation updateStation = new FireStation("123 sesame street", 2);

        mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateStation)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("FireStation updated"));
    }
    @DisplayName("Update a non existing station")
    @Test
    void testUpdateNonExistingStation() throws Exception {
        FireStation updateStation = new FireStation("123 green street", 2);

        mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStation)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("FireStation not found for the given address"));
    }
    @DisplayName("Delete a station OK")
    @Test
    void testDeleteStationOK() throws Exception {

        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(station)))
        .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.delete("/firestation/123 sesame street"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("FireStation deleted"));
    }
    @DisplayName("Delete a non existing station")
    @Test
    void testDeleteNonExistingStation() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/firestation/123 sesame street"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("No firestation is found for the given address"));
    }

}

package com.safetynet.alertsystem.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class SafetyNetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // #1 : /firestation?stationNumber=
    @DisplayName("Test URL - people at a given station")
    @Test
    void testGetPeopleAtGivenStationURL() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/firestation")
                        .param("stationNumber", "2"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // #2 : /fire?address=
    @DisplayName("Test URL - get people residing at an address")
    @Test
    void testGetPeopleResidingAtAddressURL() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/fire")
                        .param("address", "123 sesame street"))
                .andExpect(MockMvcResultMatchers.status().isOk());}

    // #3 : /personInfo?lastName=
    @DisplayName("Test URL - personal info")
    @Test
    void testGetAlertURL() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/personInfo")
                        .param("lastName", "Doe"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // #4 : /communityEmail?city=
    @DisplayName("Test URL - get emails in city")
    @Test
    void testGetEmailsInCityURL() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
                        .param("city", "Paris"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

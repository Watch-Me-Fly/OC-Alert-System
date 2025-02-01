package com.safetynet.alertsystem.controllers.alert;

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
public class AlertsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // #1 : /childAlert?address=
    @DisplayName("Test URL - Child Alert")
    @Test
    void testChildAlert() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert")
                        .param("address", String.valueOf("12 sesame street")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    // #2 : /phoneAlert?firestation=
    @DisplayName("Test URL - Phone Alert")
    @Test
    void  testPhoneAlert() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert")
                        .param("firestation", String.valueOf(1)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    // #3 : /flood/stations?stations=
    @DisplayName("Test URL - Flood Alert")
    @Test
    void testFloodAlert() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/flood/stations")
                        .param("stations", "2,3"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

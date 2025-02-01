package com.safetynet.alertsystem.services.alert;

import com.safetynet.alertsystem.model.Data;
import com.safetynet.alertsystem.model.core.*;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.UniversalService;
import com.safetynet.alertsystem.service.alerts.PhoneAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PhoneAlertServiceTest {

    @InjectMocks
    private static PhoneAlertService service;
    @Mock
    private JsonReaderRepository jsonReader;
    @Mock
    private UniversalService universalService;
    @Autowired
    private MockMvc mockMvc;

    private List<Person> peopleList;
    private List<FireStation> stationsList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        peopleList = Arrays.asList(
                new Person("John", "Doe", "123 sesame street", "Paris", 75002, "999-888-7890", "john@example.com"),
                new Person("Jane", "Doe", "123 sesame street", "Lyon", 92003, "123-456-7890", "jane@example.com")
        );
        stationsList = Arrays.asList(
                new FireStation("123 sesame street", 2),
                new FireStation("45 some avenue", 3)
        );
        Data data = new Data();
        data.setPersons(peopleList);
        data.setFirestations(stationsList);
        Mockito.doReturn(data).when(jsonReader).getData();
        service = new PhoneAlertService(jsonReader);
    }

    @DisplayName("Get phone numbers from address")
    @Test
    void getPhoneNumbersFromAddressTest() {

        List<String> result = service.generateAlert("2");

        assertNotNull(result);
        assertEquals(2, result.size());
    }
    @DisplayName("Station does not exist, empty phone list")
    @Test
    void getPhoneNumbersEmptyTest() {

        List<String> result = service.generateAlert("5");
        assertTrue(result.isEmpty());
    }

}

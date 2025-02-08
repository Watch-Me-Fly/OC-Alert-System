package com.safetynet.alertsystem.services.alert;

import com.safetynet.alertsystem.model.Data;
import com.safetynet.alertsystem.model.core.FireStation;
import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.alerts.FloodAlertService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class FloodAlertServiceTest {

    @InjectMocks
    private FloodAlertService service;
    @Mock
    private JsonReaderRepository jsonReader;

    private List<FireStation> stationsList;
    private List<Person> peopleList;
    private List<MedicalRecord> recordsList;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        stationsList = Arrays.asList(
                new FireStation("123 sesame street", 2),
                new FireStation("45 some avenue", 3)
        );
        peopleList = Arrays.asList(
                new Person("John", "Doe", "123 sesame street", "Paris", 75002, "999-888-7890", "john@example.com"),
                new Person("Jane", "Doe", "123 sesame street", "Lyon", 92003, "123-456-7890", "jane@example.com")
        );
        recordsList = Arrays.asList(
                new MedicalRecord("John", "Doe", "05/15/2010", Collections.emptyList(), Collections.emptyList()),
                new MedicalRecord("Jane", "Doe", "10/20/1985", Collections.emptyList(), Collections.emptyList())
        );
        Data data = new Data();
        data.setPersons(peopleList);
        data.setMedicalrecords(recordsList);
        data.setFirestations(stationsList);
        Mockito.doReturn(data).when(jsonReader).getData();
        service = new FloodAlertService(jsonReader);
    }


    @DisplayName("Generate a flood alert")
    @Test
    void testGenerateAlert() throws Exception {

        Map<String, Object> result = service.generateAlert("2,3");

        assertNotNull(result);
        assertEquals(2, result.size());

    }

}

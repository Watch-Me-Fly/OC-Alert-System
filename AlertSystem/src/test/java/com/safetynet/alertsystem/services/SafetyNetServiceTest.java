package com.safetynet.alertsystem.services;

import com.safetynet.alertsystem.model.Data;
import com.safetynet.alertsystem.model.core.FireStation;
import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.SafetyNetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class SafetyNetServiceTest {

    @Mock
    private JsonReaderRepository jsonReader;
    @InjectMocks
    private SafetyNetService service;

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
        service = new SafetyNetService(jsonReader);
        service.initializeLists();
    }

    // #1 : /firestation?stationNumber=
    @DisplayName("get people residing at a given station, count adults and children")
    @Test
    void testGetPeopleAtStationData() throws Exception {

        Map<String, Object> response = service.getPeopleAtStation(2);

        assertNotNull(response);
        assertEquals(3, response.size());
        assertEquals(1, response.get("adultCount"));
        assertEquals(1, response.get("childCount"));
    }

    // #2 : /fire?address=
    @DisplayName("get people residing at an address, return fire station number")
    @Test
    void testGetPeopleResidingAtAddress() throws Exception {
        Map<String, Object> response = service.getFireInfo("123 sesame street");

        assertNotNull(response);
        assertEquals(List.of(2), response.get("stationNumbersList"));
    }

    // #3 : /personInfo?lastName=
    @DisplayName("personal info")
    @Test
    void testGetPersonalInfo() throws Exception {
        List<Map<String, Object>> getPersonInfo = service.getPersonInfo("Doe");

        assertNotNull(getPersonInfo);
        assertEquals(2, getPersonInfo.size());
    }

    // #4 : /communityEmail?city=
    @DisplayName("Return Data - get emails in city")
    @Test
    void testGetEmailsInCity() throws Exception {
        List<String> emails = service.getCommunityEmails("Paris");

        assertNotNull(emails);
        assertEquals("john@example.com", emails.get(0));
    }

}

package com.safetynet.alertsystem.services.alert;

import com.safetynet.alertsystem.model.Data;
import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.UniversalService;
import com.safetynet.alertsystem.service.alerts.ChildAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ChildAlertServiceTest {

    @InjectMocks
    private ChildAlertService service;
    @Mock
    private JsonReaderRepository jsonReader;
    @Mock
    private UniversalService universalService;

    private List<Person> peopleList;
    private List<MedicalRecord> recordsList;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
        peopleList = Arrays.asList(
                new Person("John", "Doe", "123 Street", "City", 75002, "123-456-7890", "john@example.com"),
                new Person("Jane", "Doe", "123 Street", "City", 92003, "123-456-7890", "jane@example.com")
        );

        recordsList = Arrays.asList(
                new MedicalRecord("John", "Doe", "05/15/2010", Collections.emptyList(), Collections.emptyList()),
                new MedicalRecord("Jane", "Doe", "10/20/1985", Collections.emptyList(), Collections.emptyList())
        );
        Data data = new Data();
        data.setPersons(peopleList);
        data.setMedicalrecords(recordsList);
        // force jsonReader to return mock data
        Mockito.doReturn(data).when(jsonReader).getData();
        service = new ChildAlertService(jsonReader);
    }

    @DisplayName("Get children by address")
    @Test
    void testGetChildrenByAddressOK() {

        when(universalService.getAgeFromBirthdate("05/15/2010")).thenReturn(Integer.valueOf("14"));
        when(universalService.getAgeFromBirthdate("10/20/1985")).thenReturn(Integer.valueOf("39"));

        Map<String, Object> result = service.generateAlert("123 Street");
        assertNotNull(result);

        List<Map<String, String>> children = (List<Map<String, String>>) result.get("children");
        List<Person> householdMembers = (List<Person>) result.get("householdMembers");

        assertEquals(1, children.size());
        assertEquals("John", children.get(0).get("firstName"));
        assertEquals("Doe", children.get(0).get("lastName"));
        assertEquals("14", children.get(0).get("age"));

        assertEquals(1, householdMembers.size());
        assertEquals("Jane", householdMembers.get(0).getFirstName());
        assertEquals("Doe", householdMembers.get(0).getLastName());

    }

    @DisplayName("Address with no children")
    @Test
    void testGetChildrenByAddressNone() {

        Object result = service.generateAlert("456 Avenue");
        assertTrue(((Map<String, Object>) result).isEmpty());
    }
}

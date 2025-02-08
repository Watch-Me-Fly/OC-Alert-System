package com.safetynet.alertsystem.services.core;

import com.safetynet.alertsystem.model.Data;
import com.safetynet.alertsystem.model.core.FireStation;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.core.FireStationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FireStationServiceTest {

    private FireStationService fireStationService;
    private static JsonReaderRepository jsonReader;
    private static List<FireStation> fireStations;

    @BeforeAll
    static void setup() {
        jsonReader = Mockito.mock(JsonReaderRepository.class);
    }

    @BeforeEach
    void setUpForEach() {
        fireStations = Arrays.asList(
                new FireStation("123 sesame street", 2),
                new FireStation("45 some avenue", 3)
        );

        Data data = new Data();
        data.setFirestations(fireStations);
        Mockito.when(jsonReader.getData()).thenReturn(data);

        fireStationService = new FireStationService(jsonReader);
    }

    // Create
    @DisplayName("Add firestation to a list + get station by addres")
    @Test
    void addFirestationToList() {
        // arrange
        String address = "123 sesame street";
        FireStation fireStation = new FireStation(address, 2);
        // act
        fireStationService.addStation(fireStation);
        // assert
        assertEquals(2, fireStationService.getStationNbByAddress(address));
    }
    // Read
    @DisplayName("Get firestation map")
    @Test
    void getFirestationMap() {
        // act
        Map<String, Integer> fireStatoinMap =  fireStationService.getStationsMap();
        // assert
        assertNotNull(fireStatoinMap);
        assertEquals(2, fireStatoinMap.size());
    }
    @DisplayName("Get firestation by address")
    @Test
    void getFirestationByAddress() {
        // act and assert
        assertEquals(2, fireStationService.getStationNbByAddress("123 sesame street"));
    }
    // Update
    @DisplayName("Update firestation number")
    @Test
    void updateFirestationNumber() {
        // arrange
        FireStation fireStation = new FireStation("123 sesame street", 4);
        fireStationService.addStation(fireStation);
        fireStation.setStation(2);
        // act
        fireStationService.updateStationNumber(fireStation);
        // assert
        assertEquals(2, fireStationService.getStationNbByAddress(fireStation.getAddress()));
    }
    // Delete
    @DisplayName("Delete firestation")
    @Test
    void deleteFirestation() {
        // act
        fireStationService.deleteStation("123 sesame street");
        // assert
        assertFalse(fireStationService.checkIfAddressExists("123 sesame street"));
    }

}

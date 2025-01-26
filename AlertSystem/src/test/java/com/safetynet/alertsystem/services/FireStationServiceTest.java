package com.safetynet.alertsystem.services;

import com.safetynet.alertsystem.model.FireStation;
import com.safetynet.alertsystem.service.FireStationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNull;

@SpringBootTest
public class FireStationServiceTest {

    @InjectMocks
    private FireStationService fireStationService;

    @BeforeAll
    static void setup() {
    }
    @BeforeEach
    void setUpForEach() {
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
        // arrange
        FireStation fireStation = new FireStation("123 sesame street", 2);
        FireStation fireStation2 = new FireStation("456 something av.", 7);
        fireStationService.addStation(fireStation);
        fireStationService.addStation(fireStation2);
        // act
        Map<String, Integer> fireStatoinMap =  fireStationService.getStationsMap();
        // assert
        assertNotNull(fireStatoinMap);
        assertEquals(2, fireStatoinMap.size());
    }
    @DisplayName("Get firestation by address")
    @Test
    void getFirestationByAddress() {
        // arrange
        FireStation fireStation = new FireStation("123 sesame street", 2);
        FireStation fireStation2 = new FireStation("456 something av.", 7);
        fireStationService.addStation(fireStation);
        fireStationService.addStation(fireStation2);
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
        // arrange
        String address = "123 sesame street";
        FireStation fireStation = new FireStation(address, 4);
        fireStationService.addStation(fireStation);
        // act
        fireStationService.deleteStation(address);
        // assert
        assertFalse(fireStationService.checkIfAddressExists(address));
    }

}

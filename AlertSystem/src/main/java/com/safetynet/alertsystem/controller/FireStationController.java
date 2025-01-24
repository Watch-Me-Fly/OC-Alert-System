package com.safetynet.alertsystem.controller;

import com.safetynet.alertsystem.model.FireStation;
import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.FireStationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final FireStationService fireStationService;

    @Autowired
    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @GetMapping
    public Map<String, Integer> getFireStations() {
        return fireStationService.getStationsMap();
    }

    @PostMapping
    public ResponseEntity<String> addFireStation(@RequestBody FireStation fireStation) {
        fireStationService.addStation(fireStation);
        return ResponseEntity.status(HttpStatus.CREATED).body("FireStation created");
    }

    @PutMapping
    public ResponseEntity<String> updateFireStation(@RequestBody FireStation fireStation) {
        if (fireStationService.checkIfAddressExists(fireStation.getAddress())) {
            fireStationService.updateStationNumber(fireStation);
            return ResponseEntity.ok("FireStation updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("FireStation not found for the given address");
        }
    }

    @DeleteMapping("/{address}")
    public ResponseEntity<String> deleteFireStation(@PathVariable String address) {
        if (fireStationService.checkIfAddressExists(address)) {
            fireStationService.deleteStation(address);
            return ResponseEntity.ok("FireStation deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("FireStation not found for the given address");
        }
    }

}

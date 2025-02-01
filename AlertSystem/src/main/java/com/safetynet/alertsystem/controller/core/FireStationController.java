package com.safetynet.alertsystem.controller.core;

import com.safetynet.alertsystem.model.core.FireStation;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.core.FireStationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final JsonReaderRepository jsonReader;
    private final FireStationService fireStationService;
    private static final Logger logger = LogManager.getLogger(FireStationController.class);

    @Autowired
    public FireStationController(FireStationService fireStationService, JsonReaderRepository jsonReader) {
        this.fireStationService = fireStationService;
        this.jsonReader = jsonReader;
    }

    @GetMapping("/")
    public List<FireStation> getFireStationsList() {
        return jsonReader.getData().getFirestations();
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
            return ResponseEntity.status(HttpStatus.OK).body("FireStation updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("FireStation not found for the given address");
        }
    }

    @DeleteMapping("/{address}")
    public ResponseEntity<String> deleteFireStation(@PathVariable String address) {
        if (fireStationService.checkIfAddressExists(address)) {
            fireStationService.deleteStation(address);
            return ResponseEntity.status(HttpStatus.OK).body("FireStation deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No firestation is found for the given address");
        }
    }

}

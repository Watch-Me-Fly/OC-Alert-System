package com.safetynet.alertsystem.controller;

import com.safetynet.alertsystem.service.SafetyNetService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SafetyNetController {

    private final SafetyNetService safetyNetService;
    private static final Logger logger = LoggerFactory.getLogger(SafetyNetController.class);

    public SafetyNetController(SafetyNetService safetyNetService) {
        this.safetyNetService = safetyNetService;
    }
    // -- http://localhost:8080/firestation?stationNumber=<station_number>
    @GetMapping("/firestation")
    public ResponseEntity<?> getPeopleAtStation(@RequestParam("stationNumber") int stationNumber) {
        logger.info("Fetching people at station {}", stationNumber);
        return ResponseEntity.ok(safetyNetService.getPeopleAtStation(stationNumber));
    }
    // -- http://localhost:8080/fire?address=<address>
    @GetMapping("/fire")
    public ResponseEntity<?> getFireInfo(@RequestParam("address") String address) {
        logger.info("Fetching fire info for address {}", address);
        return ResponseEntity.ok(safetyNetService.getFireInfo(address));
    }
    // -- http://localhost:8080/personInfo?lastName=<lastName>
    @GetMapping("/personInfo")
    public ResponseEntity<?> getPersonInfo(@RequestParam("lastName") String lastName) {
        logger.info("Fetching person info for lastName {}", lastName);
        return ResponseEntity.ok(safetyNetService.getPersonInfo(lastName));
    }
    // -- http://localhost:8080/communityEmail?city=<city>
    @GetMapping("/communityEmail")
    public ResponseEntity<?> getCommunityEmails(@RequestParam("city") String city) {
        logger.info("Fetching community emails for city {}", city);
        return ResponseEntity.ok(safetyNetService.getCommunityEmails(city));
    }

}

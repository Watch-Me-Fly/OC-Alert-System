package com.safetynet.alertsystem.controller;

import com.safetynet.alertsystem.service.alerts.ChildAlertService;
import com.safetynet.alertsystem.service.alerts.FloodAlertService;
import com.safetynet.alertsystem.service.alerts.PhoneAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/")
public class AlertsController {

    private ChildAlertService childAlertService;
    private PhoneAlertService phoneAlertService;
    private FloodAlertService floodAlertService;
    private static final Logger logger = LogManager.getLogger(AlertsController.class);

    public AlertsController(ChildAlertService childAlertService, PhoneAlertService phoneAlertService, FloodAlertService floodAlertService) {
        this.childAlertService = childAlertService;
        this.phoneAlertService = phoneAlertService;
        this.floodAlertService = floodAlertService;
    }

    // -- http://localhost:8080/childAlert?address=<address>
    @GetMapping("/childAlert")
    public ResponseEntity<?> getChildrenByAddress(@RequestParam("address") String address) {
        logger.info("Request to get child alerts for address: " + address);
        return ResponseEntity.ok(
                childAlertService.generateAlert(address)
        );
    }

    // -- http://localhost:8080/phoneAlert?firestation=<firestation_number>
    @GetMapping("/phoneAlert")
    public ResponseEntity<?> getPhoneAlert(@RequestParam("firestation") String fireStationNumber) {
        logger.info("Request to get phone numbers for Fire Station: " + fireStationNumber);
        return ResponseEntity.ok(
                phoneAlertService.generateAlert(fireStationNumber)
        );
    }

    // -- http://localhost:8080/flood/stations?stations=<a list of station_numbers>
    @GetMapping("/flood/stations")
    public ResponseEntity<?> getFloodAlert(@RequestParam("stations") String stations) {
        logger.info("Request to get flood alerts for stations: " + stations);
        return ResponseEntity.ok(
                floodAlertService.generateAlert(stations)
        );
    }

}
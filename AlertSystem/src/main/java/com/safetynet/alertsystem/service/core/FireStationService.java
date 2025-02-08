package com.safetynet.alertsystem.service.core;

import com.safetynet.alertsystem.model.core.FireStation;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FireStationService {

    private static final Logger logger = LogManager.getLogger(FireStationService.class);
    private final Map<String, Integer> firestationMap = new HashMap<>();
    private final JsonReaderRepository jsonReader;

    // data management
    @Autowired
    public FireStationService(JsonReaderRepository jsonReader) {
        this.jsonReader = jsonReader;
        loadData();
    }
    private void loadData() {
        logger.debug("Entering loadData method");

        List<FireStation> stations = jsonReader.getData().getFirestations();
        for (FireStation fireStation : stations) {
            firestationMap.put(fireStation.getAddress(), fireStation.getStation());
        }
        logger.info("Loaded {} firestations", stations.size());
        logger.debug("Exiting loadData method");
    }
    private void saveData() {
        List<FireStation> stations = firestationMap.entrySet()
                .stream()
                .map(entry -> new FireStation(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        jsonReader.getData().setFirestations(stations);
        jsonReader.writeData();
        logger.info("Saved firestation");
    }
    // create
    public void addStation(FireStation fireStation) {
        logger.debug("Entering addStation, fireStation: {}", fireStation);

        String address = fireStation.getAddress().toLowerCase();
        firestationMap.put(address, fireStation.getStation());
        saveData();

        logger.info("added fireStation: {}", fireStation);
        logger.debug("Exiting addStation");
    }
    // read
    public int getStationNbByAddress(String address) {
        logger.debug("Entering getStationNbByAddress, address: {}", address);

        int nb = firestationMap.get(address);

        logger.info("station number at {} = {}", nb, address);
        logger.debug("Exiting getStationNbByAddress");
        return nb;
    }
    public Map<String, Integer> getStationsMap() {
        logger.debug("Entering getStationsMap");
        logger.info("station map size: {}", firestationMap.size());

        return firestationMap;
    }
    public FireStation getStationByAddress(String address) {
        logger.debug("Entering getStationByAddress, address: {}", address);

        FireStation fireStation = new FireStation(address, firestationMap.get(address));

        logger.info("station {}", address);
        logger.debug("Exiting getStationByAddress");
        return fireStation;
    }
    public boolean checkIfAddressExists(String address) {
        logger.debug("Entering checkIfAddressExists, address: {}", address);

        boolean exists = firestationMap.containsKey(address.toLowerCase());

        logger.info("adress exists ? {}", exists);
        logger.debug("Exiting checkIfAddressExists");
        return exists;
    }
    // update
    public void updateStationNumber(FireStation fireStation) {
        logger.debug("Entering updateStationNumber, fireStation: {}", fireStation);

        if (firestationMap.containsKey(fireStation.getAddress())) {
            firestationMap.put(fireStation.getAddress(), fireStation.getStation());
            saveData();
            logger.info("updated station number: {}", fireStation);
        } else {
            logger.warn("station number not found");
        }

        logger.debug("Exiting updateStationNumber");
    }
    // delete
    public void deleteStation(String address) {
        logger.debug("Entering deleteStation, address: {}", address);

        firestationMap.remove(address);
        saveData();

        logger.info("deleted station number: {}", address);
        logger.debug("Exiting deleteStation");
    }

}
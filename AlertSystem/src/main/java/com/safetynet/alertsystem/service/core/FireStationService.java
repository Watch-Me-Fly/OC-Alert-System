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
    private final Map<String, Set<Integer>> firestationMap = new HashMap<>();
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
        firestationMap.clear();
        for (FireStation fireStation : stations) {
            firestationMap
                    .computeIfAbsent(fireStation.getAddress(), k -> new HashSet<>())
                    .add(fireStation.getStation());
        }
        logger.info("Loaded {} firestations", stations.size());
        logger.debug("Exiting loadData method");
    }
    private void saveData() {
        List<FireStation> stations = firestationMap.entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().stream().map(station -> new FireStation(entry.getKey(), station)))
                .toList();
        jsonReader.getData().setFirestations(stations);
        jsonReader.writeData();
        logger.info("Saved firestation");
    }
    // General
    public boolean checkIfAddressExists(String address) {
        logger.debug("Entering checkIfAddressExists, address: {}", address);

        boolean exists = firestationMap.containsKey(address);

        logger.info("adress exists ? {}", exists);
        logger.debug("Exiting checkIfAddressExists");
        return exists;
    }
    // create
    public void addStation(FireStation fireStation) {
        logger.debug("Entering addStation, fireStation: {}", fireStation);

        loadData();
        String address = fireStation.getAddress();
        firestationMap.putIfAbsent(address, new HashSet<>());
        firestationMap.get(address).add(fireStation.getStation());
        saveData();

        logger.info("added fireStation: {}", fireStation);
        logger.debug("Exiting addStation");
    }
    // read
    public List<FireStation> getAllStations() {
        logger.debug("Entering getAllStations");

        List<FireStation> stations = firestationMap.entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(station -> new FireStation(entry.getKey(), station)))
                .toList();

        logger.info("Stations found: {}", stations.size());
        logger.debug("Exiting getAllStations");
        return stations;
    }
    public int getStationNbByAddress(String address) {
        logger.debug("Entering getStationNbByAddress, address: {}", address);

        Set<Integer> stationNumbers = firestationMap.get(address);
        // Returning only first station
        if (stationNumbers != null && !stationNumbers.isEmpty()) {
            logger.info("Station numbers for {}: {}", address, stationNumbers);
            return stationNumbers.iterator().next();
        }else {
            logger.warn("No station found for address: {}", address);
            return -1;
        }
    }
    public Map<String, Integer> getStationsMap() {
        logger.debug("Entering getStationsMap");

        // Returns only the first station per address
        Map<String, Integer> oneStationMap = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : firestationMap.entrySet()) {
            oneStationMap.put(
                    entry.getKey(),
                    entry.getValue().iterator().next()
            );
        }
        logger.info("station map size: {}", oneStationMap.size());
        return oneStationMap;
    }
    public FireStation getStationByAddress(String address) {
        logger.debug("Entering getStationByAddress, address: {}", address);

        Set<Integer> stations = firestationMap.get(address);
        // if there's at least 1 station, get it
        if (stations != null && !stations.isEmpty()) {
            return new FireStation(address, stations.iterator().next());
        }

        logger.debug("Exiting getStationByAddress");
        return null;
    }
    // update
    public void updateStationNumber(FireStation fireStation) {
        logger.debug("Entering updateStationNumber, fireStation: {}", fireStation);

        String key = fireStation.getAddress();
        Set<Integer> stations = firestationMap.get(key);
        if (stations != null) {
            stations.remove(fireStation.getStation());
            stations.add(fireStation.getStation());
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
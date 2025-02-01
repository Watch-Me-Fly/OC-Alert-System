package com.safetynet.alertsystem.service.core;

import com.safetynet.alertsystem.model.core.FireStation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
public class FireStationService {

    private static final Logger logger = LogManager.getLogger(FireStationService.class);
    private final Map<String, Integer> firestationMap = new HashMap<>();

    // create
    public void addStation(FireStation fireStation) {
        logger.debug("Entering addStation, fireStation: {}", fireStation);

        String address = fireStation.getAddress().toLowerCase();
        firestationMap.put(address, fireStation.getStation());

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

        logger.info("deleted station number: {}", address);
        logger.debug("Exiting deleteStation");
    }

}
package com.safetynet.alertsystem.service;

import com.safetynet.alertsystem.model.FireStation;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
public class FireStationService {

    private final Map<String, Integer> firestationMap = new HashMap<>();

    // create
    public void addStation(FireStation fireStation) {
        String address = fireStation.getAddress().toLowerCase();
        firestationMap.put(address, fireStation.getStation());
    }
    // read
    public int getStationNbByAddress(String address) {
        return firestationMap.get(address.toLowerCase());
    }
    public Map<String, Integer> getStationsMap() {
        return firestationMap;
    }
    public boolean checkIfAddressExists(String address) {
        return firestationMap.containsKey(address.toLowerCase());
    }
    // update
    public void updateStationNumber(FireStation fireStation) {
        if (firestationMap.containsKey(fireStation.getAddress())) {
            firestationMap.put(fireStation.getAddress(), fireStation.getStation());
        }
    }
    // delete
    public void deleteStation(String address) {
        firestationMap.remove(address);
    }

}
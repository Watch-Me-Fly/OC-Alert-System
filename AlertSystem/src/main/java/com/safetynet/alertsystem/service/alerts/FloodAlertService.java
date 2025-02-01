package com.safetynet.alertsystem.service.alerts;

import com.safetynet.alertsystem.model.core.FireStation;
import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.UniversalService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FloodAlertService implements Alert {

    private static final Logger logger = LogManager.getLogger(FloodAlertService.class);
    private final JsonReaderRepository jsonReader;
    private UniversalService universalService = new UniversalService();

    @Override
    public Map<String, Object> generateAlert(String stations) {
        logger.debug("Entering generateAlert, stations: {}", stations);

        List<Integer> stationNumbers = parseStationNumbers(stations);
        logger.info("found {} stations", stationNumbers.size());

        List<String> addressList = getAddressesforStations(stationNumbers);
        logger.info("found {} addresses", addressList.size());

        logger.debug("Exiting generateAlert");
        return generateResponse(addressList);
    }
    // Parses the list of stations passed in URL
    private List<Integer> parseStationNumbers(String stations) {
        logger.debug("Entering parseStationNumbers, stations: {}", stations);

        List<Integer> result = Arrays.stream(stations.split(","))
                        .map(Integer::parseInt)
                        .toList();

        logger.debug("Exiting parseStationNumbers");
        return result;
    }
    // Gets a list of addresses for each station number
    private List<String> getAddressesforStations(List<Integer> stationNumbers) {
        logger.debug("Entering getAddressesforStations, {} stations", stationNumbers.size());

        List<FireStation> fireStationList = jsonReader.getData().getFirestations();
        logger.info("retrieved {} firestations", fireStationList.size());

        List<String> result = fireStationList.stream()
                .filter(station -> stationNumbers.contains(station.getStation()))
                .map(FireStation::getAddress)
                .toList();

        logger.debug("Exiting getAddressesforStations");
        return result;
    }

    // make a list of people and their medical records for each address
    private List<Map<String, Object>> getPeopleInAddress(String address, List<Person> people, List<MedicalRecord> medicalRecords) {
        logger.debug("Entering getPeopleInAddress, address: {}", address);

        List<Map<String, Object>> results = people.stream()
                .filter(person -> person.getAddress().equals(address))
                .map(person -> universalService.getPersonalDetails(person, medicalRecords, true, false, false))
                .toList();

        logger.info("retrieved {} person", results.size());
        logger.debug("Exiting getPeopleInAddress");
        return results;
    }
    // response is a map with addresses as key, and a list of people as value
    private Map<String, Object> generateResponse(List<String> addresses)
    {
        logger.debug("Entering generateResponse, {} addresses", addresses.size());

        List<Person> people = jsonReader.getData().getPersons();
        List<MedicalRecord> records = jsonReader.getData().getMedicalrecords();

        Map<String, Object> response = new HashMap<>();

        for (String address : addresses){
            List<Map<String, Object>> peopleInAddress = getPeopleInAddress(address, people, records);
            response.put(address, peopleInAddress);
        }

        logger.info("retrieved {} people for {} addresses", people.size(), addresses.size());
        logger.debug("Exiting generateResponse");
        return response;
    }

}

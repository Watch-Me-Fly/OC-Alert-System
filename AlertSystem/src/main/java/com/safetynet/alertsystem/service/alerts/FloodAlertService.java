package com.safetynet.alertsystem.service.alerts;

import com.safetynet.alertsystem.model.core.FireStation;
import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.UniversalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FloodAlertService implements Alert {

    private final JsonReaderRepository jsonReader;
    private UniversalService universalService = new UniversalService();

    @Override
    public Map<String, Object> generateAlert(String stations) {

        List<Integer> stationNumbers = new ArrayList<>();
        stationNumbers = parseStationNumbers(stations);
        List<String> addressList = new ArrayList<>();
        addressList = getAddressesforStations(stationNumbers);
        return generateResponse(addressList);
    }
    // Parses the list of stations passed in URL
    private List<Integer> parseStationNumbers(String stations) {
        return Arrays.stream(stations.split(","))
                        .map(Integer::parseInt)
                        .toList();
    }
    // Gets a list of addresses for each station number
    private List<String> getAddressesforStations(List<Integer> stationNumbers) {

        List<FireStation> fireStationList = jsonReader.getData().getFirestations();

        return fireStationList.stream()
                .filter(station -> stationNumbers.contains(station.getStation()))
                .map(FireStation::getAddress)
                .toList();
    }
    /**
     * For each address, make a list of people and their medical records
     * For each person, get personal details and medical records
     */
    private List<Map<String, Object>> getPeopleInAddress(String address, List<Person> people, List<MedicalRecord> medicalRecords) {

        return people.stream()
                .filter(person -> person.getAddress().equals(address))
                .map(person -> universalService.getPersonalDetails(person, medicalRecords, true, false, false))
                .toList();
    }
    /**
     * Create a response as a map
     * key : is address
     * value : is a list of people, with their personal & medical info
     */
    private Map<String, Object> generateResponse(List<String> addresses)
    {
        List<Person> people = jsonReader.getData().getPersons();
        List<MedicalRecord> records = jsonReader.getData().getMedicalrecords();

        Map<String, Object> response = new HashMap<>();

        for (String address : addresses){
            List<Map<String, Object>> peopleInAddress = getPeopleInAddress(address, people, records);
            response.put(address, peopleInAddress);
        }
        return response;
    }

}

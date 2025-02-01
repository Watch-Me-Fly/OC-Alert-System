package com.safetynet.alertsystem.service.alerts;

import com.safetynet.alertsystem.model.core.FireStation;
import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhoneAlertService implements Alert {

    private final JsonReaderRepository jsonReader;

    @Override
    public List<String> generateAlert(String firestationNumber) {
        List<FireStation> fireStations = jsonReader.getData().getFirestations();
        List<Person> people = jsonReader.getData().getPersons();
        List<String> phoneNumbers = new ArrayList<>();

        try {
            int stationNb = Integer.parseInt(firestationNumber);

            // get fire station's address from its number
            List<FireStation> stations = fireStations.stream()
                    .filter(station -> station.getStation() == stationNb)
                    .toList();

            if (stations.isEmpty()) {
                return phoneNumbers;
            }

            // add all phone numbers for each fire station
            for (FireStation fireStation : stations) {
                people.stream()
                        .filter(person -> person.getAddress().equalsIgnoreCase(fireStation.getAddress()))
                        .forEach(person -> phoneNumbers.add(person.getPhone()));
            }

        } catch (NumberFormatException e) {
            return phoneNumbers;
        }

        return phoneNumbers.isEmpty() ? new ArrayList<>() :  phoneNumbers;
    }

}

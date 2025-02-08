package com.safetynet.alertsystem.service.alerts;

import com.safetynet.alertsystem.model.core.FireStation;
import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PhoneAlertService implements Alert {

    private static final Logger logger = LogManager.getLogger(PhoneAlertService.class);
    private final JsonReaderRepository jsonReader;

    @Override
     public Set<String> generateAlert(String firestationNumber) {
        if (firestationNumber == null) {
            return Collections.emptySet();
        }

        logger.debug("Entering generateAlert with stations {}", firestationNumber);

        List<FireStation> fireStations = jsonReader.getData().getFirestations();
        List<Person> people = jsonReader.getData().getPersons();
        Set<String> phoneNumbers = new HashSet<>();

        try {
            int stationNb = Integer.parseInt(firestationNumber);

            // get fire station's address from its number
            List<FireStation> stations = fireStations.stream()
                    .filter(station -> station.getStation() == stationNb)
                    .toList();

            if (stations.isEmpty()) {
                logger.warn("No firestation found");
                return phoneNumbers;
            } else {
                logger.info("Found {} stations", stations.size());
            }

            // add all phone numbers for each fire station
            for (FireStation fireStation : stations) {
                people.stream()
                        .filter(person -> person.getAddress().equalsIgnoreCase(fireStation.getAddress()))
                        .forEach(person -> phoneNumbers.add(person.getPhone()));
            }
            logger.info("found {} phone numbers", phoneNumbers.size());

        } catch (NumberFormatException e) {
            logger.error("Invalid phone number format", e);
            return phoneNumbers;
        }

        logger.debug("Exiting generateAlert");
        return phoneNumbers.isEmpty() ? new HashSet<>() :  phoneNumbers;
    }

}

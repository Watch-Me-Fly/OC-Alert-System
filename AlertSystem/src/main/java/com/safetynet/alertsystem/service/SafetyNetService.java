package com.safetynet.alertsystem.service;

import com.safetynet.alertsystem.model.core.FireStation;
import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.core.FireStationService;
import com.safetynet.alertsystem.service.core.MedicalRecordService;
import com.safetynet.alertsystem.service.core.PersonService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SafetyNetService {

    private static final Logger logger = LogManager.getLogger(SafetyNetService.class);
    private final JsonReaderRepository jsonReader;
    private UniversalService universalService = new UniversalService();

    List<Person> peopleList ;
    List<FireStation> stationList;
    List<MedicalRecord> recordsList;

    @PostConstruct
    public void initializeLists() {
        logger.info("Initializing lists");
        peopleList = new PersonService(jsonReader).getAllPeople();
        stationList = new FireStationService(jsonReader).getAllStations();
        recordsList = new MedicalRecordService(jsonReader).getAllMedicalRecords();
        logger.info("Lists initialized : {} persons, {} fire stations, {} medical records",
                peopleList.size(), stationList.size(), recordsList.size());
    }

    // get a list of people at the addresses of given station numbers
    public Map<String, Object> getPeopleAtStation(int stationNumber) {
        logger.debug("entering getPeopleAtStation, stationNumber: {}", stationNumber);

        // initializations
        List<Map<String, Object>> personDetails = new ArrayList<>();
        int adultCount = 0;
        int childCount = 0;

        // step 1 : find the address of the station
        List<String> addresses = stationList
                .stream()
                .filter(station -> station.getStation() == stationNumber)
                .map(FireStation::getAddress)
                .toList();
        logger.info("Station {} has {} addresses", stationNumber, addresses.size());

        // step 2 : find all people at the address found
        List<Person> peopleCovered = peopleList
                .stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .toList();
        logger.info("People covered = {} persons", peopleCovered.size());

        // step 3 : for each person, add data to list
        for (Person person : peopleCovered) {

            Map<String, Object> personInfo = universalService
                    .getPersonalDetails(person, null,true, false, true);
            personDetails.add(personInfo);

            // step 4 : get the age from medical records
            Optional<MedicalRecord> record = recordsList
                    .stream()
                    .filter(rec -> rec.getFirstName().equals(person.getFirstName())
                    && rec.getLastName().equals(person.getLastName()))
                    .findFirst();

            if (record.isPresent()) {
                int age = universalService.getAgeFromBirthdate(record.get().getBirthdate());
                if (age < 18) {
                    childCount++;
                } else {
                    adultCount++;
                }
            } else {
                logger.warn("No record found for person: {}", person.getFirstName() + " " + person.getLastName());
            }
        }

        // step 5 : categorize adults and children & return results
        Map<String, Object> response = new HashMap<>();
        response.put("people", personDetails);
        response.put("adultCount", adultCount);
        response.put("childCount", childCount);
        logger.info("Returning response : {} adults, {} children", adultCount, childCount);

        logger.debug("Exiting getPeopleAtStation");
        return response;
    }

    // return a list of people living at an address, and the number of fire station at the same address
    public Map<String, Object> getFireInfo(String address) {
        logger.debug("entering getFireInfo, address: {}", address);

        if (address == null) {
            logger.error("Address is null");
            return null;
        }

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> residentDetails = new ArrayList<>();

        List<Integer> stationNumbersList = stationList
                .stream()
                .filter(station -> station.getAddress().equals(address))
                .map(FireStation::getStation)
                .toList();
        response.put("stationNumbersList", stationNumbersList);
        logger.info("Returning response : {} stationNumbersList", stationNumbersList);

        List<Person> residentsList = peopleList
                .stream()
                .filter(person -> person.getAddress().equals(address))
                .toList();
        logger.info("Returning response : {} residents found at {}", residentsList.size(), address);

        for (Person resident : residentsList) {
            Map<String, Object> residentInfo = universalService
                    .getPersonalDetails(resident, recordsList,true, false, false);
            residentDetails.add(residentInfo);
        }
        response.put("residents", residentDetails);
        logger.debug("Exiting getFireInfo");

        return response;
    }

    // return a list of people carrying the same name, and their information
    public List<Map<String, Object>> getPersonInfo(String lastName) {
        logger.debug("entering getPersonInfo, lastName: {}", lastName);

        if (lastName == null || lastName.isEmpty()) {
            logger.error("No last name provided");
            return Collections.emptyList();
        }

        peopleList = jsonReader.getData().getPersons();
        recordsList = jsonReader.getData().getMedicalrecords();

        List<Map<String, Object>> results = peopleList
                .stream()
                .filter(person -> person.getLastName().equals(lastName))
                .map(person ->
                        universalService.getPersonalDetails(person, recordsList, false, true, true))
                .toList();
        logger.info("{} persons found carrying the last name {}", results.size(), lastName);

        logger.debug("Exiting getPersonInfo");
        return results;
    }

    // return all email addresses of residents of a given city
    public List<String> getCommunityEmails(String city) {
        logger.debug("entering getCommunityEmails, city: {}", city);

        if (city == null || city.isEmpty()) {
            logger.error("No city provided");
            return Collections.emptyList();
        }

        List<String> results = peopleList.stream()
                .filter(person -> person.getCity().equals(city))
                .map(person -> person.getEmail())
                .toList();

        logger.info("{} email found in {}", results.size(), city);

        logger.debug("Exiting getCommunityEmails");
        return results;
    }
}
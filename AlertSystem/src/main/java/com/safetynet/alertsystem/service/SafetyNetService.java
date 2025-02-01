package com.safetynet.alertsystem.service;

import com.safetynet.alertsystem.model.core.FireStation;
import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SafetyNetService {

    private final JsonReaderRepository jsonReader;
    private UniversalService universalService = new UniversalService();

    List<Person> peopleList;
    List<FireStation> stationList;
    List<MedicalRecord> recordsList;

    @PostConstruct
    public void initializeLists() {
        peopleList = jsonReader.getData().getPersons();
        stationList = jsonReader.getData().getFirestations();
        recordsList = jsonReader.getData().getMedicalrecords();
    }

    // get a list of people at the addresses of given station numbers
    public Map<String, Object> getPeopleAtStation(int stationNumber) {
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

        // step 2 : find all people at the address found
        List<Person> peopleCovered = peopleList
                .stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .toList();

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
            }
        }

        // step 5 : categorize adults and children & return results
        Map<String, Object> response = new HashMap<>();
        response.put("people", personDetails);
        response.put("adultCount", adultCount);
        response.put("childCount", childCount);

        return response;
    }

    // return a list of people living at an address, and the number of fire station at the same address
    public Map<String, Object> getFireInfo(String address) {

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> residentDetails = new ArrayList<>();

        List<Integer> stationNumbersList = stationList
                .stream()
                .filter(station -> station.getAddress().equals(address))
                .map(FireStation::getStation)
                .toList();
        response.put("stationNumbersList", stationNumbersList);

        List<Person> residentsList = peopleList
                .stream()
                .filter(person -> person.getAddress().equals(address))
                .toList();

        for (Person resident : residentsList) {
            Map<String, Object> residentInfo = universalService
                    .getPersonalDetails(resident, recordsList,true, false, false);
            residentDetails.add(residentInfo);
        }
        response.put("residents", residentDetails);

        return response;
    }

    // return a list of people carrying the same name, and their information
    public List<Map<String, Object>> getPersonInfo(String lastName) {

        peopleList = jsonReader.getData().getPersons();
        recordsList = jsonReader.getData().getMedicalrecords();

        return peopleList
                .stream()
                .filter(person -> person.getLastName().equals(lastName))
                .map(person ->
                        universalService.getPersonalDetails(person, recordsList, false, true, true))
                .toList();
    }

    // return all email addresses of residents of a given city
    public List<String> getCommunityEmails(String city) {
        return peopleList.stream()
                .filter(person -> person.getCity().equals(city))
                .map(person -> person.getEmail())
                .toList();
    }
}
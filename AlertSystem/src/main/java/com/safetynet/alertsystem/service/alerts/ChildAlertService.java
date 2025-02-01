package com.safetynet.alertsystem.service.alerts;

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
public class ChildAlertService implements Alert {

    private static final Logger logger = LogManager.getLogger(ChildAlertService.class);
    private final UniversalService universalService = new UniversalService();
    private final JsonReaderRepository jsonReader;

    @Override
    public Map<String, Object> generateAlert(String address) {
        logger.debug("entering generateAlert, address: {}", address);

        List<Person> persons = jsonReader.getData().getPersons();
        List<MedicalRecord> medicalRecords = jsonReader.getData().getMedicalrecords();
        logger.info("{} persons found, and {} medical records", persons.size(), medicalRecords.size());

        // get all people living at the given address
        List<Person> residents = persons.stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        if (residents.isEmpty()) {
            logger.warn("No resident found for address {}", address);
            return Collections.emptyMap();
        } else {
            logger.info("{} residents found", residents.size());
        }

        // prepare lists
        List<Map<String, String>> children = new ArrayList<>();
        List<Map<String, String>> otherMembers = new ArrayList<>();

        // put residents into either : children or other members list
        for (Person person : residents) {
            medicalRecords.stream()
                    .filter(record ->
                            record.getFirstName()
                                    .equalsIgnoreCase(person.getFirstName())
                            && record.getLastName()
                                    .equalsIgnoreCase(person.getLastName()))
                    .forEach(record -> {
                        sortIntoGroups(record, person, children, otherMembers);
                    });
        }

        if (children.isEmpty()) {
            logger.warn("No children found for address {}", address);
            return Collections.emptyMap();
        } else {
            logger.info("{} children found", children.size());
        }

        // return response
        Map<String, Object> response = new HashMap<>();
        response.put("children", children);
        response.put("householdMembers", otherMembers);

        logger.debug("exiting generateAlert");
        return response;

    }

    // get age, then sort into lists
    private void sortIntoGroups(MedicalRecord record,
                                  Person person,
                                  List<Map<String, String>> children,
                                  List<Map<String, String>> otherMembers)
    {
        logger.debug("entering sortIntoGroups, name : {}", person.getFirstName() + " " + person.getLastName());

        int age = universalService.getAgeFromBirthdate(record.getBirthdate());
        logger.info("age = {}", age);

        if (age <= 18) {
            Map<String, String> child = new HashMap();
            child.put("firstName", person.getFirstName());
            child.put("lastName", person.getLastName());
            child.put("age", String.valueOf(age));
            children.add(child);
            logger.info("{} is sorted as child", person.getFirstName());
        } else {
            Map<String, String> member = new HashMap();
            member.put("name", person.getFirstName() + " " + person.getLastName());
            member.put("age", String.valueOf(age));
            otherMembers.add(member);
            logger.info("{} is sorted as adult", person.getFirstName());
        }

        logger.debug("exiting sortIntoGroups");
    }

}
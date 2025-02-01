package com.safetynet.alertsystem.service.alerts;


import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.UniversalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChildAlertService implements Alert {

    private final UniversalService universalService = new UniversalService();
    private final JsonReaderRepository jsonReader;

    @Override
    public Map<String, Object> generateAlert(String address) {

        List<Person> persons = jsonReader.getData().getPersons();
        List<MedicalRecord> medicalRecords = jsonReader.getData().getMedicalrecords();

        // get all people living at the given address
        List<Person> residents = persons.stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        if (residents.isEmpty()) {
            return Collections.emptyMap();
        }

        // prepare lists
        List<Map<String, String>> children = new ArrayList<>();
        List<Person> otherMembers = new ArrayList<>();

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
            return Collections.emptyMap();
        }

        // return response
        Map<String, Object> response = new HashMap<>();
        response.put("children", children);
        response.put("householdMembers", otherMembers);

        return response;

    }

    /**
     * Find age for each person from medical record
     * then sort into lists
     */
    private void sortIntoGroups(MedicalRecord record,
                                  Person person,
                                  List<Map<String, String>> children,
                                  List<Person> otherMembers)
    {
        int age = universalService.getAgeFromBirthdate(record.getBirthdate());
        if (age <= 18) {
            Map<String, String> child = new HashMap();
            child.put("firstName", person.getFirstName());
            child.put("lastName", person.getLastName());
            child.put("age", String.valueOf(age));
            children.add(child);
        } else {
            otherMembers.add(person);
        }
    }

}
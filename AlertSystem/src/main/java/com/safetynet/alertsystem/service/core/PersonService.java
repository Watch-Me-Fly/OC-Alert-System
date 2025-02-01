package com.safetynet.alertsystem.service.core;

import com.safetynet.alertsystem.model.core.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    private static final Logger logger = LogManager.getLogger(PersonService.class);
    private final List<Person> people = new ArrayList<>();

    public boolean checkIfPersonExists(String firstName, String lastName) {
        logger.debug("Checking if {} exists", firstName + " " + lastName);
        for (Person person : people) {
            if (person.getFirstName().equals(firstName)
                    && person.getLastName().equals(lastName)) {
                logger.info("Person found");
                return true;
            }
        }
        logger.debug("Person does not exist");
        return false;
    }
    // create
    public Person addPerson(Person person) {
        logger.debug("Entering addPerson, name : {}", person.getFirstName() + " " + person.getLastName());

        people.add(person);

        logger.info("Person added");
        logger.debug("Exiting addPerson");
        return person;
    }
    // read
    public List<Person> getAllPeopleByName(String firstName, String lastName)
    {
        logger.debug("entering getAllPeopleByName, name : {}", firstName + " " + lastName);

        List<Person> persons = new ArrayList<>();

        for (Person person : people) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                persons.add(person);
            }
        }

        logger.info("{} Persons found", persons.size());
        logger.debug("exiting getAllPeopleByName");
        return persons;
    }
    // update
    public Person updatePerson(Person updatedPerson)
    {
        logger.debug("Entering updatePerson, name : {}", updatedPerson.getFirstName() + " " + updatedPerson.getLastName());

        for (Person person : people) {
            if (person.getFirstName().equals(updatedPerson.getFirstName()) && person.getLastName().equals(updatedPerson.getLastName())) {
                person.setAddress(updatedPerson.getAddress());
                person.setCity(updatedPerson.getCity());
                person.setZip(updatedPerson.getZip());
                person.setPhone(updatedPerson.getPhone());
                person.setEmail(updatedPerson.getEmail());
                logger.info("Person updated");
                return person;
            }
        }
        logger.info("Person cannot be updated because does not exist");
        logger.debug("Exiting updatePerson");
        return null;
    }
    // delete
    public boolean deletePerson(String firstName, String lastName) {

        logger.debug("Entering deletePerson, name : {}", firstName + " " + lastName);

        boolean result = people.removeIf(person ->
                person.getFirstName().equals(firstName)
                        && person.getLastName().equals(lastName)
        );

        if (result) {
            logger.info("Person deleted");
        } else {
            logger.info("Person cannot be deleted because does not exist");
        }
        logger.debug("Exiting deletePerson");
        return result;
    }

}

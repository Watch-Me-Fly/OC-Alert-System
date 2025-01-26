package com.safetynet.alertsystem.service.core;

import com.safetynet.alertsystem.model.core.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    private final List<Person> people = new ArrayList<>();

    public boolean checkIfPersonExists(String firstName, String lastName) {
        for (Person person : people) {
            if (person.getFirstName().equals(firstName)
                    && person.getLastName().equals(lastName)) {
                return true;
            }
        }
        return false;
    }
    // create
    public Person addPerson(Person person) {
        people.add(person);
        return person;
    }
    // read
    public List<Person> getAllPeopleByName(String firstName, String lastName)
    {
        List<Person> persons = new ArrayList<>();

        for (Person person : people) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                persons.add(person);
            }
        }
        return persons;
    }
    // update
    public Person updatePerson(Person updatedPerson)
    {
        for (Person person : people) {
            if (person.getFirstName().equals(updatedPerson.getFirstName()) && person.getLastName().equals(updatedPerson.getLastName())) {
                person.setAddress(updatedPerson.getAddress());
                person.setCity(updatedPerson.getCity());
                person.setZip(updatedPerson.getZip());
                person.setPhone(updatedPerson.getPhone());
                person.setEmail(updatedPerson.getEmail());
                return person;
            }
        }
        return null;
    }
    // delete
    public boolean deletePerson(String firstName, String lastName) {
        return people.removeIf(person ->
                person.getFirstName().equals(firstName)
                        && person.getLastName().equals(lastName)
        );
    }

}

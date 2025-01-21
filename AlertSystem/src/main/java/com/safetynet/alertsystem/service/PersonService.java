package com.safetynet.alertsystem.service;

import com.safetynet.alertsystem.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    private final List<Person> people = new ArrayList<>();

    // create
    public Person addPerson(Person person) {
        people.add(person);
        return person;
    }
    // read
    public List<Person> getAllPeopleByName(String firstName, String lastName) {
        for (Person person : people) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                people.add(person);
            }
        }
        return people;
    }
    // update
    public Person updatePerson(String firstName, String lastName, Person updatedPerson) {
        for (Person person : people) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
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

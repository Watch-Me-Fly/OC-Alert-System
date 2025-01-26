package com.safetynet.alertsystem.controller;

import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final JsonReaderRepository jsonReader;
    private final PersonService personService;
    private static final Logger logger = LogManager.getLogger(PersonController.class);

    @Autowired
    public PersonController(PersonService personService, JsonReaderRepository jsonReader) {
        this.personService = personService;
        this.jsonReader = jsonReader;
    }

    @GetMapping("/list")
    public List<Person> getPersons() {
        return jsonReader.getData().getPersons();
    }

    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        personService.addPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body("Person added");
    }

    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody Person person) {
        if (personService.checkIfPersonExists(person.getFirstName(), person.getLastName()))
        {
        personService.updatePerson(person);
            return ResponseEntity.status(HttpStatus.OK).body("Person updated");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        if (personService.checkIfPersonExists(firstName, lastName))
        {
           personService.deletePerson(firstName, lastName);
           return ResponseEntity.status(HttpStatus.OK).body("Person deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
    }

}
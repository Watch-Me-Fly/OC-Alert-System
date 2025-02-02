package com.safetynet.alertsystem.controller.core;

import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.core.PersonService;
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

    @GetMapping("/")
    public List<Person> getPersons() {
        logger.debug("Entering getPersons");

        List<Person> peopleList = jsonReader.getData().getPersons();

        logger.debug("Exiting getPersons");
        return peopleList;
    }

    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        logger.debug("Entering addPerson");

        personService.addPerson(person);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).body("Person added");

        logger.info("added person");
        logger.debug("Exiting addPerson");
        return response;
    }

    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody Person person) {
        logger.debug("Entering updatePerson");

        if (personService.checkIfPersonExists(person.getFirstName(), person.getLastName()))
        {
        personService.updatePerson(person);
            logger.info("updated person");
            return ResponseEntity.status(HttpStatus.OK).body("Person updated");
        }
        logger.error("Person does not exist");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        logger.debug("Entering deletePerson");
        if (personService.checkIfPersonExists(firstName, lastName))
        {
           personService.deletePerson(firstName, lastName);
            logger.info("deleted person");
           return ResponseEntity.status(HttpStatus.OK).body("Person deleted");
        }
        logger.error("Person does not exist");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
    }

}
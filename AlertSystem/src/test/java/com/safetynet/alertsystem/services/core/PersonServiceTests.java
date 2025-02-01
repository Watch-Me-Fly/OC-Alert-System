package com.safetynet.alertsystem.services.core;

import com.safetynet.alertsystem.model.core.Person;
import com.safetynet.alertsystem.service.core.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonServiceTests {

    @Mock
    private PersonService personService;
    private Person person1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setAddress("23 random street");
        person1.setCity("Paris");
        person1.setZip(75012);
        person1.setEmail("john@doe.com");
    }

    // Create
    @DisplayName("Add a person to a list")
    @Test
    public void addPersonToList() {
        // arrange
        when(personService.addPerson(person1)).thenReturn(person1);
        // act
        Person result = personService.addPerson(person1);
        // assert
        String notNullMessage = "Person is null";
        String isJohnMessage = "First name is John";

        assertNotNull(result, notNullMessage);
        assertEquals("John", result.getFirstName(), isJohnMessage);
    }
    // Read
    @DisplayName("Get all person with the same name")
    @Test
    public void getAllPeopleByNameTest() {
        // arrange
        Person person2 = new Person();
        person2.setFirstName("John");
        person2.setLastName("Doe");
        person2.setAddress("12 south avenue");
        person2.setCity("Marseille");
        person2.setZip(12435);
        person2.setEmail("johnny@doe.com");

        Person person3 = new Person();
        person3.setFirstName("Jane");
        person3.setLastName("Doe");
        person3.setAddress("89 west street");
        person3.setCity("Lyon");
        person3.setZip(67890);
        person3.setEmail("jane@doe.com");

        List<Person> mockList = Arrays.asList(person1, person2);
        when(personService.getAllPeopleByName("John", "Doe")).thenReturn(mockList);

        // act
        List<Person> result = personService.getAllPeopleByName("John", "Doe");

        // assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    // Update
    @DisplayName("Update person's info")
    @Test
    public void updatePersonInfo() {
        // arrange
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Doe");
        updatedPerson.setAddress("12 south avenue");
        updatedPerson.setCity("Marseille");
        updatedPerson.setZip(12435);
        updatedPerson.setEmail("johnny@doe.com");

        when(personService.updatePerson(updatedPerson)).thenReturn(updatedPerson);

        // act
        Person result = personService.updatePerson(updatedPerson);

        // assert
        assertNotNull(result);
        assertEquals("Marseille", result.getCity());
    }
    // Delete
    @DisplayName("Delete person's info")
    @Test
    public void deletePersonInfo() {
        // arrange
        when(personService.deletePerson("John", "Doe")).thenReturn(true);
        // act
        boolean result = personService.deletePerson("John", "Doe");
        // assert
        assertTrue(result);
    }
    @DisplayName("Delete a non existing person")
    @Test
    public void deleteNonExistingPerson() {
        when(personService.deletePerson("John", "Doe")).thenReturn(false);
        boolean result = personService.deletePerson("John", "Doe");
        assertFalse(result);
    }
}

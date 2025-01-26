package com.safetynet.alertsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alertsystem.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonControllerTests {

    @Mock
    private Person person;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("23 random street");
        person.setCity("Paris");
        person.setZip(75012);
        person.setEmail("john@doe.com");
    }

    private void addPerson(Person person) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Person added"));
    }

    @DisplayName("/person/list Endpoint = 200 [OK]")
    @Test
    void testPersonListOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/person/list")).andExpect(MockMvcResultMatchers.status().isOk());
    }
    @DisplayName("Add a person - OK")
    @Test
    void testAddPersonOK() throws Exception {
        addPerson(person);
    }
    @DisplayName("Update a person that exists")
    @Test
    void testUpdatePersonOK() throws Exception {
        addPerson(person);

        Person updatePerson = new Person();
        updatePerson.setFirstName("John");
        updatePerson.setLastName("Doe");
        updatePerson.setAddress("23 avenue");

        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePerson)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Person updated"));

    }
    @DisplayName("Update a non existing person")
    @Test
    void testUpdateNonExistingPerson() throws Exception {
        Person updatePerson = new Person();
        updatePerson.setFirstName("Dina");
        updatePerson.setLastName("Doe");
        updatePerson.setAddress("23 random street");

        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePerson)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Person not found"));
    }
    @DisplayName("Delete a person OK")
    @Test
    void testDeletePersonOK() throws Exception {
        addPerson(person);

        mockMvc.perform(MockMvcRequestBuilders.delete("/person/John/Doe"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Person deleted"));
    }
    @DisplayName("Delete a non existing person")
    @Test
    void testDeleteNonExistingPerson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/person/John/Doe"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Person not found"));
    }

}
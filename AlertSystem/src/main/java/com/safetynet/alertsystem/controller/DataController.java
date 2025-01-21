package com.safetynet.alertsystem.controller;

import com.safetynet.alertsystem.model.Data;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class DataController {

    private final JsonReaderRepository jsonReader;

    public DataController(JsonReaderRepository jsonReader) {
        this.jsonReader = jsonReader;
    }

    @GetMapping("")
    public Data getData() {
        Data data = jsonReader.getData();
        if (data == null) {
            throw new RuntimeException("failed to read data");
        }
        return data;
    }
}

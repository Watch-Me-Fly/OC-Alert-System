package com.safetynet.alertsystem.controller.core;

import com.safetynet.alertsystem.model.core.MedicalRecord;
import com.safetynet.alertsystem.repository.JsonReaderRepository;
import com.safetynet.alertsystem.service.core.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordsController {

    private JsonReaderRepository jsonReader;
    private final MedicalRecordService medicalRecordService;
    private static final Logger logger = LogManager.getLogger(MedicalRecordsController.class);

    @Autowired
    public MedicalRecordsController(MedicalRecordService medicalRecordService, JsonReaderRepository jsonReader) {
        this.medicalRecordService = medicalRecordService;
        this.jsonReader = jsonReader;
    }

    @GetMapping("/")
    public List<MedicalRecord> getMedicalRecords() {
        return jsonReader.getData().getMedicalrecords();
    }

    @PostMapping
    public ResponseEntity<String> addRecord(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.addMedicalRecord(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body("Medical record added");
    }

    @PutMapping
    public ResponseEntity<String> updateRecord(@RequestBody MedicalRecord medicalRecord) {
        if (medicalRecordService.checkIfRecordExists(medicalRecord.getFirstName(), medicalRecord.getLastName())) {
            medicalRecordService.updateMedicalRecord(medicalRecord);
            return ResponseEntity.status(HttpStatus.OK).body("Medical record updated");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medical record not found");
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deleteRecord(@PathVariable String firstName, @PathVariable String lastName) {
        if (medicalRecordService.checkIfRecordExists(firstName, lastName)) {
            medicalRecordService.deleteMedicalRecord(firstName, lastName);
            return ResponseEntity.status(HttpStatus.OK).body("Medical record deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medical record not found");
    }
}

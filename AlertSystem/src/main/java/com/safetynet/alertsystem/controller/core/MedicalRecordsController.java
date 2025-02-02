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
        logger.debug("Entering getMedicalRecords");

        List<MedicalRecord> medicalRecords = jsonReader.getData().getMedicalrecords();

        logger.info("found {} medical records", medicalRecords.size());
        logger.debug("Exiting getMedicalRecords");

        return medicalRecords;
    }

    @PostMapping
    public ResponseEntity<String> addRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.debug("Entering addRecord");

        medicalRecordService.addMedicalRecord(medicalRecord);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).body("Medical record added");

        logger.info("Medical record added");
        logger.debug("Exiting addRecord");
        return response;
    }

    @PutMapping
    public ResponseEntity<String> updateRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.debug("Entering updateRecord");

        if (medicalRecordService.checkIfRecordExists(medicalRecord.getFirstName(), medicalRecord.getLastName())) {

            medicalRecordService.updateMedicalRecord(medicalRecord);
            logger.info("Medical record updated");
            return ResponseEntity.status(HttpStatus.OK).body("Medical record updated");

        }
        logger.error("Medical record does not exist");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medical record not found");
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deleteRecord(@PathVariable String firstName, @PathVariable String lastName) {
        logger.debug("Entering deleteRecord");
        if (medicalRecordService.checkIfRecordExists(firstName, lastName)) {

            medicalRecordService.deleteMedicalRecord(firstName, lastName);
            logger.info("Medical record deleted");
            return ResponseEntity.status(HttpStatus.OK).body("Medical record deleted");
        }
        logger.error("Medical record does not exist");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medical record not found");
    }
}

package com.lc.dentalcore.infrastructure.input.controller;

import com.lc.dentalcore.application.dto.PatientDTO;
import com.lc.dentalcore.application.handler.IPatientHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientHandler patientHandler;

    @PostMapping()
    public ResponseEntity<Void> createPatient(@RequestBody PatientDTO patientDTO){
        patientHandler.createPatient(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}

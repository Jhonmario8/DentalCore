package com.lc.dentalcore.infrastructure.input.controller;

import com.lc.dentalcore.application.dto.PatientDTO;
import com.lc.dentalcore.application.handler.IPatientHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientHandler patientHandler;

    @PostMapping()
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO){
        PatientDTO dto = patientHandler.createPatient(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        PatientDTO dto = patientHandler.updatePatient(id, patientDTO);
        return ResponseEntity.ok(dto);
    }
}

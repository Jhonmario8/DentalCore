package com.lc.dentalcore.infrastructure.input.controller;

import com.lc.dentalcore.application.dto.PatientDTO;
import com.lc.dentalcore.application.handler.IPatientHandler;
import jakarta.validation.Valid;
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
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO){
        PatientDTO dto = patientHandler.createPatient(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO dto = patientHandler.updatePatient(id, patientDTO);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public ResponseEntity<?> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(patientHandler.findAll(name));
    }
    @PatchMapping("/{id}/inactivate")
    public ResponseEntity<Void> inactivatePatient(@PathVariable Long id) {
        patientHandler.inactivatePatient(id);
        return ResponseEntity.ok().build();
    }
}

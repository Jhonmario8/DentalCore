package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.PatientDTO;

import java.util.List;

public interface IPatientHandler {
    PatientDTO createPatient(PatientDTO patientDTO);
    PatientDTO updatePatient(Long id, PatientDTO patientDTO);
    List<PatientDTO> findAll(String name);
    void inactivatePatient(Long id);
}

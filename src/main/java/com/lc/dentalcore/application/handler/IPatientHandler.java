package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.PatientDTO;

public interface IPatientHandler {
    PatientDTO createPatient(PatientDTO patientDTO);
    PatientDTO updatePatient(Long id, PatientDTO patientDTO);
}

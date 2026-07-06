package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.api.IPatientServicePort;
import com.lc.dentalcore.domain.model.Patient;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PatientService implements IPatientServicePort {

    private final IPatientPersistencePort patientPersistencePort;

    @Override
    public void createPatient(Patient patient) {
        patient.validate();
        patientPersistencePort.savePatient(patient);
    }
}

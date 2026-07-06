package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.api.IPatientServicePort;
import com.lc.dentalcore.domain.exception.EmailAlreadyExistsException;
import com.lc.dentalcore.domain.exception.IdentificationNumberAlreadyExistException;
import com.lc.dentalcore.domain.exception.PhoneNumberAlreadyExist;
import com.lc.dentalcore.domain.model.Patient;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PatientService implements IPatientServicePort {

    private final IPatientPersistencePort patientPersistencePort;

    @Override
    public void createPatient(Patient patient) {
        patient.validate();
        validateUniquePatient(patient);
        patientPersistencePort.savePatient(patient);
    }

    private void validateUniquePatient(Patient patient) {
        if (patient.getEmail() != null) {
            if (patientPersistencePort.findByEmail(patient.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException();
            }
        }
        if (patientPersistencePort.findByIdentificationNumber(patient.getIdentificationNumber()).isPresent()) {
            throw new IdentificationNumberAlreadyExistException();
        }
        if (patientPersistencePort.existsByPhoneNumber(patient.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExist();
        }
    }
}

package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.api.IPatientServicePort;
import com.lc.dentalcore.domain.exception.EmailAlreadyExistsException;
import com.lc.dentalcore.domain.exception.IdentificationNumberAlreadyExistException;
import com.lc.dentalcore.domain.exception.PatientNotFoundException;
import com.lc.dentalcore.domain.exception.PhoneNumberAlreadyExist;
import com.lc.dentalcore.domain.model.Patient;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PatientService implements IPatientServicePort {

    private final IPatientPersistencePort patientPersistencePort;

    @Override
    public Patient createPatient(Patient patient) {
        patient.validate();
        validateUniquePatient(patient);
        return patientPersistencePort.savePatient(patient);

    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {
        Patient existsPatient = patientPersistencePort.findById(id).orElseThrow(PatientNotFoundException::new);
        patient.validate();
        existsPatient.setName(patient.getName());
        existsPatient.setEmail(patient.getEmail());
        existsPatient.setPhoneNumber(patient.getPhoneNumber());
        existsPatient.setIdentificationNumber(patient.getIdentificationNumber());
        existsPatient.setBirthDate(patient.getBirthDate());
        existsPatient.setNotes(patient.getNotes());
        return patientPersistencePort.savePatient(existsPatient);
    }

    @Override
    public List<Patient> findAll(String name) {

        return  (name != null && !name.isBlank())
                ? patientPersistencePort.findAllByName(name)
                : patientPersistencePort.findAll();

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

package com.lc.dentalcore.domain.spi;

import com.lc.dentalcore.domain.model.Patient;

import java.util.Optional;

public interface IPatientPersistencePort {
    void savePatient(Patient patient);
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByIdentificationNumber(String identificationNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}

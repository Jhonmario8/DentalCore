package com.lc.dentalcore.domain.spi;

import com.lc.dentalcore.domain.model.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientPersistencePort {
    Patient savePatient(Patient patient);
    Optional<Patient> findById(Long id);
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByIdentificationNumber(String identificationNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    List<Patient> findAllByName(String name);
    List<Patient> findAll();
}

package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.Patient;

import java.util.List;

public interface IPatientServicePort {

    Patient createPatient(Patient patient);
    Patient updatePatient(Long id, Patient patient);
    List<Patient> findAll(String name);
    void inactivatePatient(Long id);
}

package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.Patient;

public interface IPatientServicePort {

    void createPatient(Patient patient);
}

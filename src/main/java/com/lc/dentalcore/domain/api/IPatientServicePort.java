package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.Patient;

public interface IPatientServicePort {

    Patient createPatient(Patient patient);
    Patient updatePatient(Long id, Patient patient);

}

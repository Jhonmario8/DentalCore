package com.lc.dentalcore.domain.spi;

import com.lc.dentalcore.domain.model.Patient;

public interface IPatientPersistencePort {
    void savePatient(Patient patient);
}

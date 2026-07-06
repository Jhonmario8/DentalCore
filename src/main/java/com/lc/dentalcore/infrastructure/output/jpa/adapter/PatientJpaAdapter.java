package com.lc.dentalcore.infrastructure.output.jpa.adapter;

import com.lc.dentalcore.domain.model.Patient;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import com.lc.dentalcore.infrastructure.output.jpa.mapper.IPatientEntityMapper;
import com.lc.dentalcore.infrastructure.output.jpa.repository.IPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientJpaAdapter implements IPatientPersistencePort {

    private final IPatientRepository patientRepository;
    private final IPatientEntityMapper mapper;


    @Override
    public void savePatient(Patient patient) {
        patientRepository.save(mapper.toEntity(patient));
    }
}

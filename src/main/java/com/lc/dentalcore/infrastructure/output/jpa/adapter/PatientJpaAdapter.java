package com.lc.dentalcore.infrastructure.output.jpa.adapter;

import com.lc.dentalcore.domain.model.Patient;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import com.lc.dentalcore.infrastructure.output.jpa.mapper.IPatientEntityMapper;
import com.lc.dentalcore.infrastructure.output.jpa.repository.IPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientJpaAdapter implements IPatientPersistencePort {

    private final IPatientRepository patientRepository;
    private final IPatientEntityMapper mapper;


    @Override
    public Patient savePatient(Patient patient) {
        return mapper.toDomain(patientRepository.save(mapper.toEntity(patient)));
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email)
                .map(mapper::toDomain);

    }

    @Override
    public Optional<Patient> findByIdentificationNumber(String identificationNumber) {
        return patientRepository.findByIdentificationNumber(identificationNumber)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return patientRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id)
                .map(mapper::toDomain);
    }
}

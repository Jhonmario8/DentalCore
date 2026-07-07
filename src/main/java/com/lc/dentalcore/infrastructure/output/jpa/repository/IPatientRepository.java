package com.lc.dentalcore.infrastructure.output.jpa.repository;

import com.lc.dentalcore.infrastructure.output.jpa.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPatientRepository extends JpaRepository<PatientEntity, Long> {
    Optional<PatientEntity> findByEmail(String email);
    Optional<PatientEntity> findByIdentificationNumber(String identificationNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    List<PatientEntity> findByNameContainingIgnoreCaseAndActiveIs(String name, Boolean active);

}

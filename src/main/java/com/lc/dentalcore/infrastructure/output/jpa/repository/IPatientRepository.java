package com.lc.dentalcore.infrastructure.output.jpa.repository;

import com.lc.dentalcore.infrastructure.output.jpa.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPatientRepository extends JpaRepository<PatientEntity, Long> {

}

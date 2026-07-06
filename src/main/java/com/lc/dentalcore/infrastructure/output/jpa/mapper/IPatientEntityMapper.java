package com.lc.dentalcore.infrastructure.output.jpa.mapper;

import com.lc.dentalcore.domain.model.Patient;
import com.lc.dentalcore.infrastructure.output.jpa.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IPatientEntityMapper {

    Patient toDomain(PatientEntity patientEntity);

    PatientEntity toEntity(Patient patient);
}

package com.lc.dentalcore.application.mapper;

import com.lc.dentalcore.application.dto.PatientDTO;
import com.lc.dentalcore.domain.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IPatientMapper {

    Patient toDomain(PatientDTO patientDTO);

}

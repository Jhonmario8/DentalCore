package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.PatientDTO;
import com.lc.dentalcore.application.mapper.IPatientMapper;
import com.lc.dentalcore.domain.api.IPatientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientHandler implements IPatientHandler{

    private final IPatientServicePort patientServicePort;
    private final IPatientMapper patientMapper;

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        return patientMapper.toDTO(patientServicePort.createPatient(patientMapper.toDomain(patientDTO)));
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        return patientMapper.toDTO(patientServicePort.updatePatient(id, patientMapper.toDomain(patientDTO)));
    }

    @Override
    public List<PatientDTO> findAll(String name) {
        return patientServicePort.findAll(name).stream().map(patientMapper::toDTO).toList();
    }

    @Override
    public void inactivatePatient(Long id) {
        patientServicePort.inactivatePatient(id);
    }
}

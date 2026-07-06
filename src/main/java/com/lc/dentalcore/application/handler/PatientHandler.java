package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.PatientDTO;
import com.lc.dentalcore.application.mapper.IPatientMapper;
import com.lc.dentalcore.domain.api.IPatientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientHandler implements IPatientHandler{

    private final IPatientServicePort patientServicePort;
    private final IPatientMapper patientMapper;

    @Override
    public void createPatient(PatientDTO patientDTO) {
        patientServicePort.createPatient(patientMapper.toDomain(patientDTO));
    }
}

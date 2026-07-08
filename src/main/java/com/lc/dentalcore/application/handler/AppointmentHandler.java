package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.AppointmentDTO;
import com.lc.dentalcore.application.mapper.IAppointmentMapper;
import com.lc.dentalcore.domain.api.IAppointmentServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentHandler implements IAppointmentHandler {

    private final IAppointmentServicePort appointmentServicePort;
    private final IAppointmentMapper mapper;

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        return mapper.toDto(appointmentServicePort.createAppointment(mapper.toDomain(appointmentDTO))) ;
    }

}

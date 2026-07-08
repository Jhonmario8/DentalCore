package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.AppointmentDTO;
import com.lc.dentalcore.application.mapper.IAppointmentMapper;
import com.lc.dentalcore.domain.api.IAppointmentServicePort;
import com.lc.dentalcore.domain.model.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentHandler implements IAppointmentHandler {

    private final IAppointmentServicePort appointmentServicePort;
    private final IAppointmentMapper mapper;

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        return mapper.toDto(appointmentServicePort.createAppointment(mapper.toDomain(appointmentDTO))) ;
    }

    @Override
    public AppointmentDTO updateStatus(Long id, AppointmentStatus status) {
        return mapper.toDto(appointmentServicePort.updateStatus(id, status));
    }

    @Override
    public List<AppointmentDTO> findAllByDate(LocalDate date) {
        return appointmentServicePort.findAllByDate(date)
                .stream()
                .map(mapper::toDto)
                .toList();
    }


}

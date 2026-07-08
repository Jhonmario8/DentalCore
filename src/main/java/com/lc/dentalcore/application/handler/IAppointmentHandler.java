package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.AppointmentDTO;
import com.lc.dentalcore.domain.model.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentHandler {

    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    AppointmentDTO updateStatus(Long id, AppointmentStatus status);
    List<AppointmentDTO> findAllByDate(LocalDate date);
}

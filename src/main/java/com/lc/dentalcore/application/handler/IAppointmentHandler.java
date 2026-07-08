package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.AppointmentDTO;
import com.lc.dentalcore.domain.model.AppointmentStatus;

public interface IAppointmentHandler {

    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    AppointmentDTO updateStatus(Long id, AppointmentStatus status);
}

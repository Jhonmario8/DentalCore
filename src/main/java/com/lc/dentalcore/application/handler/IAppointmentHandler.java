package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.AppointmentDTO;

public interface IAppointmentHandler {

    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
}

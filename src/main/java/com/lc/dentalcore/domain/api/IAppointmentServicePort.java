package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.Appointment;
import com.lc.dentalcore.domain.model.AppointmentStatus;

public interface IAppointmentServicePort {
    Appointment createAppointment(Appointment appointment);
    Appointment updateStatus(Long id, AppointmentStatus status);
}

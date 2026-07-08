package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.Appointment;

public interface IAppointmentServicePort {
    Appointment createAppointment(Appointment appointment);
}

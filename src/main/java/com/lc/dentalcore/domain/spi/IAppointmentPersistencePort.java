package com.lc.dentalcore.domain.spi;

import com.lc.dentalcore.domain.model.Appointment;

import java.util.List;

public interface IAppointmentPersistencePort {
    Appointment saveAppointment(Appointment appointment);
    List<Appointment> findAll();
}
